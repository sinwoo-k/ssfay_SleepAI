# model_loader.py
import tensorflow as tf
from tensorflow.keras.models import load_model

def focal_loss(alpha, gamma=1.0):
    def loss_fn(y_true, y_pred):
        y_true_oh = tf.one_hot(tf.cast(y_true, tf.int32),
                               depth=tf.shape(y_pred)[-1])
        p_t = tf.reduce_sum(y_true_oh * y_pred, axis=-1)
        a_t = tf.reduce_sum(y_true_oh * alpha,   axis=-1)
        ce  = -tf.math.log(tf.clip_by_value(p_t, 1e-7, 1))
        return tf.reduce_mean(a_t * tf.pow(1-p_t, gamma) * ce)
    return loss_fn

# 학습 때 사용한 alpha
alpha = tf.constant([0.20, 0.30, 0.15, 0.25, 0.10], tf.float32)

# 파일명 sleepony.h5로 로드, 변수명도 sleepony 로 바꿈
sleephony = load_model(
    "sleephony.h5",
    custom_objects={"loss_fn": focal_loss(alpha)}
)

# 레이블 맵
LABELS = ['N1', 'N2', 'N3', 'R', 'W']
