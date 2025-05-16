package com.c208.sleephony.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static BusinessException insufficientData() {
        return new BusinessException("BR", "수면 레벨 데이터가 충분하지 않아 리포트를 생성할 수 없습니다.");
    }
}