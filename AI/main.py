# main.py
from fastapi import FastAPI
from schema import RawSequence
from model_loader import sleepony, LABELS   # 모델 변수 sleepony 임포트
import numpy as np

app = FastAPI(title="Sleepony Prediction API")  # 앱 이름도 바꿔봄

@app.post("/predict_raw")
def predict_raw(seq: RawSequence):
    feats = []
    for w in seq.windows:
        acc = np.sqrt(np.array(w.acc_x)**2
                     + np.array(w.acc_y)**2
                     + np.array(w.acc_z)**2)
        mean_acc, std_acc   = acc.mean(), acc.std()
        mean_temp, std_temp = np.mean(w.temp),  np.std(w.temp)
        mean_hr,   std_hr   = np.mean(w.hr),    np.std(w.hr)

        ibi   = 60.0 / np.array(w.hr)
        rmssd = np.sqrt(np.mean(np.diff(ibi)**2))
        sdnn  = np.std(ibi)

        freqs = np.fft.rfftfreq(len(acc), d=1/64)
        psd   = np.abs(np.fft.rfft(acc))**2
        delta = psd[(freqs>=0.5)&(freqs<4 )].sum()
        theta = psd[(freqs>=4  )&(freqs<8 )].sum()
        alpha = psd[(freqs>=8  )&(freqs<12)].sum()

        feats.append([
          mean_acc, std_acc,
          mean_temp, std_temp,
          mean_hr, std_hr,
          rmssd, sdnn,
          delta, theta, alpha
        ])

    X = np.expand_dims(np.array(feats, dtype=np.float32), axis=0)  # (1,5,11)

    # sleepony 모델로 예측
    preds = sleepony.predict(X)
    idx   = int(np.argmax(preds, axis=-1)[0])

    return {
      "sleep_stage": LABELS[idx],
      "confidence": float(preds[0][idx])
    }