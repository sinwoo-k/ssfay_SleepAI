# model_loader.py
import os
import tensorflow as tf

MODEL_PATH = os.path.join(os.path.dirname(__file__), "sleephony.keras")
if not os.path.exists(MODEL_PATH):
    raise FileNotFoundError(f"전체 모델(.keras)이 없습니다: {MODEL_PATH}")

# compile=False 로 로드하면 custom_objects 없이도 바로 사용 가능합니다.
sleephony = tf.keras.models.load_model(MODEL_PATH, compile=False)

# 예측 후 매핑할 라벨
LABELS = ["N1", "N2", "N3", "R", "W"]
