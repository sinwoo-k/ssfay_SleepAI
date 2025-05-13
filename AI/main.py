# main.py
from fastapi import FastAPI
from pydantic import BaseModel
import numpy as np
from model_loader import sleephony, LABELS

app = FastAPI()

class Payload(BaseModel):
    features: list  # ex) [[ [feat1,...,feat11], ... (seq_len=5) ], ... ]

@app.get("/health")
def health():
    return {"status": "ok"}

@app.post("/predict")
def predict(payload: Payload):
    arr = np.array(payload.features, dtype=np.float32)
    preds = sleephony.predict(arr)
    idxs = np.argmax(preds, axis=1)
    return {"labels": [LABELS[i] for i in idxs]}
