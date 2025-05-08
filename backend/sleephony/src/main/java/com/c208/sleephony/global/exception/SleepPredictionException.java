package com.c208.sleephony.global.exception;

public class SleepPredictionException extends RuntimeException {
    public SleepPredictionException(String message) {
        super(message);
    }

    public SleepPredictionException(String message, Throwable cause) {
        super(message, cause);
    }
}