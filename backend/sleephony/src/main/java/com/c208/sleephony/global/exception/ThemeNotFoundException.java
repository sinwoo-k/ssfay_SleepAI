package com.c208.sleephony.global.exception;

public class ThemeNotFoundException extends RuntimeException {

    public ThemeNotFoundException(Integer themeId) {
        super("존재하지 않는 테마입니다. ID = " + themeId);
    }
}
