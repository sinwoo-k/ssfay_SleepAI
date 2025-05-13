# 1) 베이스 이미지
FROM python:3.10-slim

# 2) 작업 디렉터리
WORKDIR /app

# 3) 의존성 설치
COPY requirements.txt .
RUN pip install --upgrade pip \
 && pip install --no-cache-dir -r requirements.txt

# 4) 애플리케이션 코드 및 모델 복사
COPY . .

# 5) 컨테이너 시작 시 uvicorn 으로 FastAPI 기동
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
