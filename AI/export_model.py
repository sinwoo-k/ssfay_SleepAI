# export_model.py
import tensorflow as tf
from tensorflow.keras.models import load_model

if __name__ == "__main__":
    # 1) 기존 HDF5 가중치 파일 로드
    model = load_model("sleephony.weights.h5", compile=False)
    # 2) SavedModel 형식으로 export
    tf.saved_model.save(model, "sleephony_savedmodel")
    print("✅ SavedModel 형식으로 sleephony_savedmodel/ 폴더 생성 완료")
