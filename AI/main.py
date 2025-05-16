"""
====================================================================
 Sleephony – RAW-sequence inference micro-service
  • FastAPI + aiokafka + TensorFlow/Keras
  • Consumes   sleep-stage-raw-request   (RawPayload)
  • Produces   sleep-stage-raw-response  (RawResponse)
====================================================================
"""
from __future__ import annotations

import asyncio, logging, os
from typing import List

import numpy as np
import pandas as pd
from dotenv                    import load_dotenv
from fastapi                   import FastAPI
from pydantic                  import BaseModel, Field
from aiokafka                  import AIOKafkaConsumer, AIOKafkaProducer
from sklearn.preprocessing     import StandardScaler
from model_loader              import sleephony, LABELS   # ← 이미 만들어 둔 모델 로더

# ──────────────────────────────────────────────────────────────────
load_dotenv(".env")

logging.basicConfig(level=logging.INFO,
                    format="%(asctime)s  %(message)s")
logger = logging.getLogger("sleephony")

# Kafka connection -------------------------------------------------
KAFKA_BOOTSTRAP = os.getenv("KAFKA_BOOTSTRAP", "kafka:9092")
REQUEST_TOPIC   = os.getenv("REQUEST_TOPIC",  "sleep-stage-raw-request")
RESPONSE_TOPIC  = os.getenv("RESPONSE_TOPIC", "sleep-stage-raw-response")
GROUP_ID        = os.getenv("GROUP_ID",       "sleepony-fastapi-group")

# signal / model hyper-params -------------------------------------
SAMPLING_RATE = 20        # Hz
EPOCH_SECONDS = 30
STEP_SECONDS  = 15
SEQ_LEN       = 5

EPOCH_SIZE = SAMPLING_RATE * EPOCH_SECONDS     # 600
STEP       = SAMPLING_RATE * STEP_SECONDS      # 300

# FastAPI app ------------------------------------------------------
app = FastAPI(
    title     = "Sleephony AI API",
    version   = "0.2.0",
    root_path = "/ai"        # <- reverse-proxy prefix (필요 없으면 제거)
)

# Pydantic models --------------------------------------------------
class RawPayload(BaseModel):
    acc_x: List[float] = Field(..., min_length=EPOCH_SIZE)
    acc_y: List[float]
    acc_z: List[float]
    temp:  List[float]
    hr:    List[float]

class RawResponse(BaseModel):
    requestId: str
    labels:    List[str]

# Kafka handles ----------------------------------------------------
producer: AIOKafkaProducer | None = None
consumer: AIOKafkaConsumer | None = None

# Health-check -----------------------------------------------------
@app.get("/health")
def health():
    return {"status": "ok"}

# ───────────────────────────── lifecycle ──────────────────────────
@app.on_event("startup")
async def startup_event():
    global producer, consumer

    producer = AIOKafkaProducer(
        bootstrap_servers=KAFKA_BOOTSTRAP,
        value_serializer=lambda v: v.json().encode("utf-8")
    )
    await producer.start()

    consumer = AIOKafkaConsumer(
        REQUEST_TOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP,
        group_id=GROUP_ID,
        value_deserializer=lambda b: RawPayload.parse_raw(b)
    )
    await consumer.start()

    asyncio.create_task(process_loop())
    logger.info("✅ Kafka producer / consumer ready")


@app.on_event("shutdown")
async def shutdown_event():
    await consumer.stop()
    await producer.stop()
    logger.info("🛑 Kafka closed")

# ────────────────────────────── main loop ─────────────────────────
async def process_loop():
    """
    RawPayload → feature window → sequence → stage labels
    """
    logger.info("🔄 Listening …")

    async for msg in consumer:
        payload: RawPayload = msg.value
        hdrs   = {k: v.decode() for k, v in msg.headers}
        req_id = hdrs.get("requestId", "unknown")
        user_id= hdrs.get("userId",    "")

        # 0) frame-up -----------------------------------------------------------
        df = pd.DataFrame({
            "ACC_X": payload.acc_x,
            "ACC_Y": payload.acc_y,
            "ACC_Z": payload.acc_z,
            "TEMP":  payload.temp,
            "HR":    payload.hr
        }).dropna()

        if len(df) < EPOCH_SIZE:
            await send_error(req_id, f"샘플은 최소 {EPOCH_SIZE}개 필요합니다")
            continue

        # 1) derived channels ---------------------------------------------------
        df["ACC_MAG"] = np.sqrt(df.ACC_X**2 + df.ACC_Y**2 + df.ACC_Z**2)

        # 2) window → features --------------------------------------------------
        feats: list[list[float]] = []

        for start in range(0, len(df) - EPOCH_SIZE + 1, STEP):
            seg = df.iloc[start : start + EPOCH_SIZE]

            # HR: 0 → nan  → ffill → bfill  (여전히 nan 남으면 skip)
            hr = seg.HR.replace(0, np.nan).ffill().bfill().values
            if np.isnan(hr).any():
                continue

            acc = seg.ACC_MAG.values
            tmp = seg.TEMP.values

            # basic stats
            basic = [acc.mean(), acc.std(),
                     tmp.mean(), tmp.std(),
                     hr.mean(),  hr.std()]

            # HRV
            ibi   = 60.0 / hr
            rmssd = np.sqrt(np.mean(np.diff(ibi)**2))
            sdnn  = np.std(ibi)

            # accel power bands
            freqs = np.fft.rfftfreq(len(acc), d=1/SAMPLING_RATE)
            psd   = np.abs(np.fft.rfft(acc))**2
            delta = psd[(freqs>=0.5) & (freqs<4)].sum()
            theta = psd[(freqs>=4 ) & (freqs<8)].sum()
            alpha = psd[(freqs>=8 ) & (freqs<12)].sum()
            beta  = psd[(freqs>=12  ) & (freqs<30)].sum()
            gamma = psd[freqs>=30].sum()
            feats.append(basic + [rmssd, sdnn, delta, theta, alpha, beta, gamma])

        feats = np.asarray(feats, dtype=np.float32)
        logger.info(SEQ_LEN)
        logger.info(f"[{req_id}] {feats.shape} features extracted")
        if feats.shape[0] == 0:
            await send_error(req_id, "에포크 윈도우가 하나도 생성되지 않았습니다")
            continue

        scaler = StandardScaler()
        feats  = scaler.fit_transform(feats)

        MIN_SEQ = 5
        if feats.shape[0] < MIN_SEQ:
            pad = np.repeat(feats[-1:], MIN_SEQ - feats.shape[0], axis=0)
            feats = np.concatenate([feats, pad], axis=0)
            seqs  = feats[np.newaxis, ...]          # (1, 5, 11)
        else:
            seqs = np.stack([feats[i:i+MIN_SEQ]
                            for i in range(len(feats)-MIN_SEQ+1)])

        preds  = sleephony.predict(seqs, verbose=0)
        labels = [LABELS[int(i)] for i in np.argmax(preds, axis=1)]
        # New: 로깅을 통해 라벨 출력
        logger.info(f"[{req_id}] Predicted labels: {labels}")

        # 5) publish ------------------------------------------------------------
        resp = RawResponse(requestId=req_id, labels=labels)
        await producer.send_and_wait(
            RESPONSE_TOPIC,
            resp,
            headers=[
                ("requestId", req_id.encode()),
                ("userId",    user_id.encode())
            ]
        )
        logger.info(f"[{req_id}] → {len(labels)} labels sent")

# ──────────────────────────── error helper ────────────────────────
async def send_error(request_id: str, message: str):
    err = RawResponse(requestId=request_id, labels=[])
    await producer.send_and_wait(
        RESPONSE_TOPIC,
        err,
        headers=[
            ("requestId", request_id.encode()),
            ("error",     message.encode())
        ]
    )
    logger.error(f"[{request_id}] ❌ {message}")
    return