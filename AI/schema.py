# schema.py
from pydantic import BaseModel
from typing import List

class RawWindow(BaseModel):
    acc_x: List[float]
    acc_y: List[float]
    acc_z: List[float]
    temp:  List[float]
    hr:    List[float]

class RawSequence(BaseModel):
    windows: List[RawWindow]  # 반드시 길이 5개여야 함은 로직에서 체크
