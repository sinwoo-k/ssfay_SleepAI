package com.c208.sleephony.global.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super("해당 유저를 찾을 수 없습니다. id = " + userId);
    }
}
