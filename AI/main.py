# main.py

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing    import List
import numpy as np
import pandas as pd
import asyncio, logging

from aiokafka import AIOKafkaConsumer, AIOKafkaProducer
from sklearn.preprocessing import StandardScaler
from model_loader import sleephony, LABELS               # ← 기존 모델·라벨 로더

# ── 로깅 ────────────────────────────────────────────────────────────
logging.basicConfig(level=logging.INFO, format="%(asctime)s  %(message)s")
logger = logging.getLogger("sleephony")

# ── Kafka 설정 ─────────────────────────────────────────────────────
KAFKA_BOOTSTRAP = "k12c208.p.ssafy.io:29092"
REQUEST_TOPIC  = "sleep-stage-raw-request"
RESPONSE_TOPIC = "sleep-stage-raw-response"
GROUP_ID       = "sleepony-fastapi-group"

# ── 신호 처리 파라미터 (20 Hz 기준) ──────────────────────────────────
SAMPLING_RATE = 20          # 20 samples / sec
EPOCH_SECONDS = 30          # 한 에포크 30 초
STEP_SECONDS  = 15          # 50 % overlap
SEQ_LEN       = 5           # 모델 입력 시퀀스 길이

EPOCH_SIZE = SAMPLING_RATE * EPOCH_SECONDS   # 600
STEP       = SAMPLING_RATE * STEP_SECONDS    # 300

# ── FastAPI 인스턴스 ────────────────────────────────────────────────
app = FastAPI(
    title="Sleephony API",
    version="0.1.0",
    root_path="/ai"          # ↔ 리버스 프록시 경로에 맞춰둔 예시
)

# ── Pydantic 모델 ──────────────────────────────────────────────────
class RawPayload(BaseModel):
    acc_x: List[float]
    acc_y: List[float]
    acc_z: List[float]
    temp:  List[float]
    hr:    List[float]

class RawResponse(BaseModel):
    requestId: str
    labels:    List[str]

# ── Kafka 프로듀서/컨슈머 핸들 ──────────────────────────────────────
producer:  AIOKafkaProducer | None = None
consumer:  AIOKafkaConsumer | None = None

# ── 헬스 체크 ───────────────────────────────────────────────────────
@app.get("/health")
def health():
    return {"status": "ok"}

# ── 스타트업 / 셧다운 훅 ────────────────────────────────────────────
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
    logger.info("✅ Kafka producer / consumer started")

@app.on_event("shutdown")
async def shutdown_event():
    await consumer.stop()
    await producer.stop()
    logger.info("🛑 Kafka connections closed")

# ── 메인 처리 루프 ──────────────────────────────────────────────────
async def process_loop():
    """
    1) `sleep-stage-raw-request` 토픽에서 RawPayload 수신
    2) 시계열 → feature → sequence 변환 후 모델 추론
    3) 결과를 `sleep-stage-raw-response` 토픽으로 송신
    """
    logger.info("🔄 Waiting for raw‑sequence messages ...")

    async for msg in consumer:
        payload: RawPayload = msg.value
        headers = {k: v.decode() for k, v in msg.headers}
        req_id  = headers.get("requestId", "unknown")
        user_id = headers.get("userId")           # ← 여기서 꺼내야 합니다!

        # 0. DataFrame & 길이 검사 -------------------------------------------------
        df = pd.DataFrame({
            "ACC_X": payload.acc_x,
            "ACC_Y": payload.acc_y,
            "ACC_Z": payload.acc_z,
            "TEMP":  payload.temp,
            "HR":    payload.hr
        }).dropna()

        if len(df) < EPOCH_SIZE:
            logger.warning(f"[{req_id}] too few samples ({len(df)})")
            await send_error(req_id, f"샘플은 최소 {EPOCH_SIZE}개 필요합니다")
            continue

        # 1. 가속도 벡터 크기 ------------------------------------------------------
        df["ACC_MAG"] = np.sqrt(df.ACC_X**2 + df.ACC_Y**2 + df.ACC_Z**2)

        # 2. 윈도잉 & 피처 추출 ----------------------------------------------------
        feats = []
        for start in range(0, len(df) - EPOCH_SIZE + 1, STEP):
            seg = df.iloc[start : start + EPOCH_SIZE]
            acc = seg.ACC_MAG.values
            tmp = seg.TEMP.values
            hr  = seg.HR.values

            basic = [acc.mean(), acc.std(), tmp.mean(), tmp.std(), hr.mean(), hr.std()]

            ibi   = 60.0 / hr
            rmssd = np.sqrt(np.mean(np.diff(ibi)**2))
            sdnn  = np.std(ibi)

            freqs = np.fft.rfftfreq(len(acc), d=1 / SAMPLING_RATE)
            psd   = np.abs(np.fft.rfft(acc))**2
            delta = psd[(freqs>=0.5) & (freqs<4 )].sum()
            theta = psd[(freqs>=4  ) & (freqs<8 )].sum()
            alpha = psd[(freqs>=8  ) & (freqs<12)].sum()

            feats.append(basic + [rmssd, sdnn, delta, theta, alpha])

        feats = np.asarray(feats, dtype=np.float32)

        if feats.shape[0] < SEQ_LEN:
            await send_error(req_id, f"에포크 수가 부족해 seq_len={SEQ_LEN} 구성 불가")
            continue

        # 3. 정규화 & 시퀀스 생성 ---------------------------------------------------
        feats = StandardScaler().fit_transform(feats)
        seqs  = np.stack([feats[i:i+SEQ_LEN] for i in range(len(feats) - SEQ_LEN + 1)])

        # 4. 모델 예측 --------------------------------------------------------------
        preds   = sleephony.predict(seqs)
        labels  = [LABELS[int(i)] for i in np.argmax(preds, 1)]

        # 5. Kafka 응답 -------------------------------------------------------------
        response = RawResponse(requestId=req_id, labels=labels)
        await producer.send_and_wait(
            RESPONSE_TOPIC,
            response,
            headers=[
                ("requestId", req_id.encode()),
                ("userId",    user_id.encode())    # ← fastapi 응답에도 userId 헤더 추가
                ]
        )
        logger.info(f"[{req_id}] 🏁 sent {len(labels)} labels")

# ── 오류 응답 유틸 ──────────────────────────────────────────────────
async def send_error(request_id: str, message: str):
    err_resp = RawResponse(requestId=request_id, labels=[])
    await producer.send_and_wait(
        RESPONSE_TOPIC,
        err_resp,
        headers=[
            ("requestId", request_id.encode()),
            ("error",     message.encode())
        ]
    )
    logger.error(f"[{request_id}] ❌ {message}")
