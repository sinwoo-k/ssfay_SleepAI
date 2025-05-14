# # main.py

# from fastapi import FastAPI, HTTPException
# from pydantic import BaseModel
# from typing import List
# import numpy as np
# import pandas as pd
# import asyncio
# import logging
# from aiokafka import AIOKafkaConsumer, AIOKafkaProducer
# from sklearn.preprocessing import StandardScaler

# from model_loader import sleephony, LABELS

# logging.basicConfig(level=logging.INFO)
# logger = logging.getLogger("sleephony")

# KAFKA_BOOTSTRAP = "k12c208.p.ssafy.io:29092"
# REQUEST_TOPIC  = "sleep-stage-raw-request"
# RESPONSE_TOPIC = "sleep-stage-raw-response"
# GROUP_ID       = "sleepony-fastapi-group"
# app = FastAPI(title="Sleephony API", version="0.1.0", root_path="/ai")

# # 20Hz 기준 파라미터
# SAMPLING_RATE = 20
# EPOCH_SECONDS = 30
# STEP_SECONDS  = 15
# SEQ_LEN       = 5

# EPOCH_SIZE = EPOCH_SECONDS * SAMPLING_RATE
# STEP       = STEP_SECONDS  * SAMPLING_RATE

# class RawPayload(BaseModel):
#     acc_x: List[float]
#     acc_y: List[float]
#     acc_z: List[float]
#     temp:  List[float]
#     hr:    List[float]

# class RawResponse(BaseModel):
#     requestId: str
#     labels:    List[str]

# producer: AIOKafkaProducer = None
# consumer: AIOKafkaConsumer = None

# class FeaturePayload(BaseModel):
#     # 기존 predict: 이미 [batch, seq_len=5, feat_dim=11] 형태의 피처
#     features: List[List[List[float]]]


# class RawPayload(BaseModel):
#     # 새로 추가된 predict_raw: 원본 시계열 데이터
#     acc_x: List[float]
#     acc_y: List[float]
#     acc_z: List[float]
#     temp: List[float]
#     hr: List[float]


# @app.get("/health")
# def health():
#     return {"status": "ok"}

# # -- FastAPI 이벤트 훅: startup/shutdown ---------------------------
# @app.on_event("startup")
# async def startup_event():
#     global producer, consumer

#     # 1) Kafka Producer 초기화
#     producer = AIOKafkaProducer(
#         bootstrap_servers=KAFKA_BOOTSTRAP,
#         value_serializer=lambda v: v.json().encode("utf-8")
#     )
#     await producer.start()

#     # 2) Kafka Consumer 초기화
#     consumer = AIOKafkaConsumer(
#         REQUEST_TOPIC,
#         bootstrap_servers=KAFKA_BOOTSTRAP,
#         group_id=GROUP_ID,
#         value_deserializer=lambda b: RawPayload.parse_raw(b)
#     )
#     await consumer.start()

#     # 3) 백그라운드 태스크로 메시지 처리 루프 실행
#     asyncio.create_task(process_loop())


# @app.on_event("shutdown")
# async def shutdown_event():
#     # 앱 종료 시 Kafka 리소스 정리
#     await consumer.stop()
#     await producer.stop()


# # -- 메시지 처리 루프 -----------------------------------------
# async def process_loop():
#     """
#     1) REQUEST_TOPIC에서 RawPayload 수신
#     2) 기존 predict_raw 로직 수행 → labels 리스트 생성
#     3) RESPONSE_TOPIC으로 RawResponse 전송 (헤더에 requestId 포함)
#     """
#     logger.info("[🔄] Kafka consumer listening for messages...")

#     async for msg in consumer:
#         payload: RawPayload = msg.value
#         headers = dict((k, v.decode()) for k, v in msg.headers)
#         request_id = headers.get("requestId", "")

#         logger.info("[📩] Received message from Kafka")
#         logger.info(f"[🔖] requestId: {request_id}, header keys: {list(headers.keys())}")

#         # 0) DataFrame 생성 & 길이 체크
#         df = pd.DataFrame({
#             "ACC_X": payload.acc_x,
#             "ACC_Y": payload.acc_y,
#             "ACC_Z": payload.acc_z,
#             "TEMP":  payload.temp,
#             "HR":    payload.hr
#         }).dropna()
#         logger.info(f"[📊] Received {len(df)} samples")

#         if len(df) < EPOCH_SIZE:
#             logger.warning(f"[⚠️] Too few samples ({len(df)}), min required is {EPOCH_SIZE}.")
#             raise HTTPException(
#                 status_code=400,
#                 detail=f"데이터가 너무 짧습니다. 최소 {EPOCH_SIZE} 샘플 필요"
#             )

#         # 1) ACC_MAG 계산
#         df["ACC_MAG"] = np.sqrt(df.ACC_X**2 + df.ACC_Y**2 + df.ACC_Z**2)

#         # 2) 윈도잉 & 피처 추출
#         feats = []
#         for start in range(0, len(df) - EPOCH_SIZE + 1, STEP):
#             seg = df.iloc[start : start + EPOCH_SIZE]
#             acc = seg.ACC_MAG.values
#             tmp = seg.TEMP.values
#             hr  = seg.HR.values

#             basic = [
#                 acc.mean(), acc.std(),
#                 tmp.mean(), tmp.std(),
#                 hr.mean(),  hr.std()
#             ]
#             ibi   = 60.0 / hr
#             rmssd = np.sqrt(np.mean(np.diff(ibi)**2))
#             sdnn  = np.std(ibi)

#             freqs = np.fft.rfftfreq(len(acc), d=1/SAMPLING_RATE)
#             psd   = np.abs(np.fft.rfft(acc))**2
#             delta = psd[(freqs>=0.5)&(freqs<4 )].sum()
#             theta = psd[(freqs>=4  )&(freqs<8 )].sum()
#             alpha = psd[(freqs>=8  )&(freqs<12)].sum()

#             feats.append(basic + [rmssd, sdnn, delta, theta, alpha])

#         feats = np.array(feats, dtype=np.float32)
#         logger.info(f"[📐] Extracted {len(feats)} feature vectors")

#         if feats.shape[0] < SEQ_LEN:
#             logger.warning(f"[⚠️] Not enough epochs ({feats.shape[0]}) to create sequences.")
#             raise HTTPException(
#                 status_code=400,
#                 detail=f"에포크 수가 부족하여 seq_len={SEQ_LEN}를 구성할 수 없습니다"
#             )

#         # 3) 정규화 & 시퀀스 생성
#         scaler = StandardScaler()
#         feats  = scaler.fit_transform(feats)
#         seqs   = [feats[i:i+SEQ_LEN] for i in range(len(feats)-SEQ_LEN+1)]
#         arr    = np.stack(seqs)  # shape=(n_seq, SEQ_LEN, feature_dim)
#         logger.info(f"[🤖] Running inference on {arr.shape[0]} sequences")

#         # 4) 모델 예측
#         preds = sleephony.predict(arr)
#         idxs  = np.argmax(preds, axis=1)
#         labels = [LABELS[int(i)] for i in idxs]
#         logger.info(f"[✅] Predicted labels: {labels[:5]}{'...' if len(labels)>5 else ''}")

#         # 5. 응답 전송
#         response = RawResponse(requestId=request_id, labels=labels)
#         await producer.send_and_wait(
#             RESPONSE_TOPIC,
#             response,
#             headers=[("requestId", request_id.encode("utf-8"))]
#         )
#         logger.info(f"[📤] Response sent to Kafka with requestId: {request_id}")
    
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import numpy as np
import pandas as pd
from sklearn.preprocessing import StandardScaler

from model_loader import sleephony, LABELS

app = FastAPI(title="Sleephony API", version="0.1.0")

EPOCH_SECONDS = 30
STEP_SECONDS  = 15
SEQ_LEN       = 5
SAMPLING_RATE = 20  # ← 20Hz 로 데이터 받는다 가정

EPOCH_SIZE = EPOCH_SECONDS * SAMPLING_RATE
STEP       = STEP_SECONDS  * SAMPLING_RATE

class RawPayload(BaseModel):
    acc_x: List[float]
    acc_y: List[float]
    acc_z: List[float]
    temp:  List[float]
    hr:    List[float]

@app.get("/health")
def health():
    return {"status": "ok"}

@app.post("/api/ai/sleep-stage")
def predict_raw(payload: RawPayload):
    # 1. 데이터프레임 생성 & 길이 체크
    df = pd.DataFrame({
        "ACC_X": payload.acc_x,
        "ACC_Y": payload.acc_y,
        "ACC_Z": payload.acc_z,
        "TEMP":  payload.temp,
        "HR":    payload.hr
    }).dropna()
    if len(df) < EPOCH_SIZE:
        raise HTTPException(400, f"데이터가 너무 짧습니다. 최소 {EPOCH_SIZE} 샘플 필요")

    # 2. ACC_MAG 계산
    df["ACC_MAG"] = np.sqrt(df.ACC_X**2 + df.ACC_Y**2 + df.ACC_Z**2)

    # 3. 윈도잉 & 피처 추출
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
        freqs = np.fft.rfftfreq(len(acc), d=1/SAMPLING_RATE)
        psd   = np.abs(np.fft.rfft(acc))**2
        delta = psd[(freqs>=0.5)&(freqs<4 )].sum()
        theta = psd[(freqs>=4  )&(freqs<8 )].sum()
        alpha = psd[(freqs>=8  )&(freqs<12)].sum()
        feats.append(basic + [rmssd, sdnn, delta, theta, alpha])

    feats = np.array(feats, dtype=np.float32)
    if feats.shape[0] < SEQ_LEN:
        raise HTTPException(400, f"에포크 수가 부족하여 seq_len={SEQ_LEN}를 구성할 수 없습니다")

    # 4. 정규화 & 시퀀스 생성
    feats  = StandardScaler().fit_transform(feats)
    seqs   = [feats[i:i+SEQ_LEN] for i in range(len(feats)-SEQ_LEN+1)]
    arr    = np.stack(seqs)  # (n_seq, SEQ_LEN, feat_dim)

    # 5. 예측
    preds = sleephony.predict(arr)
    idxs  = np.argmax(preds, axis=1)
    labels = [LABELS[i] for i in idxs]

    return {"labels": labels}