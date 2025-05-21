from pydantic import BaseModel, conlist
from typing import List

# 30초 × 20Hz = 600 샘플
SAMPLES_PER_WINDOW = 30 * 20
SEQ_LEN = 5

class RawWindow(BaseModel):
    acc_x: conlist(float, min_items=SAMPLES_PER_WINDOW, max_items=SAMPLES_PER_WINDOW)
    acc_y: conlist(float, min_items=SAMPLES_PER_WINDOW, max_items=SAMPLES_PER_WINDOW)
    acc_z: conlist(float, min_items=SAMPLES_PER_WINDOW, max_items=SAMPLES_PER_WINDOW)
    temp:  conlist(float, min_items=SAMPLES_PER_WINDOW, max_items=SAMPLES_PER_WINDOW)
    hr:    conlist(float, min_items=SAMPLES_PER_WINDOW, max_items=SAMPLES_PER_WINDOW)

class RawSequence(BaseModel):
    windows: conlist(RawWindow, min_items=SEQ_LEN, max_items=SEQ_LEN)
