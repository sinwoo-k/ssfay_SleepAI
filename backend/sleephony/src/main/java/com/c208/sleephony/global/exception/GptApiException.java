package com.c208.sleephony.global.exception;

public class GptApiException extends RuntimeException {
    public GptApiException(String message,Throwable cause) {
        super(message, cause);
    }
}
