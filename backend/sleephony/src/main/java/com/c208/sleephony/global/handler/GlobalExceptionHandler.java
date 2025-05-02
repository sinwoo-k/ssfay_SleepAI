package com.c208.sleephony.global.handler;

import com.c208.sleephony.global.exception.UserNotFoundException;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 에러 코드 & 기본 메시지
    @Getter
    public enum ErrorCode {
        NOT_FOUND("NF", "요청한 리소스를 찾을 수 없습니다."),
        UNAUTHORIZED("UR", "인증 정보가 없습니다."),
        FORBIDDEN("FB", "접근 권한이 없습니다."),
        BAD_REQUEST("BR", "잘못된 요청입니다."),
        INTERNAL_SERVER_ERROR("IS", "서버 오류가 발생했습니다.");

        private final String code;
        private final String message;

        ErrorCode(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }

    // 제네릭 와일드카드 적용
    private ResponseEntity<ApiResponse<?>> buildErrorResponse(HttpStatus status, ErrorCode errorCode, String customMessage) {
        String message = (customMessage != null ? customMessage : errorCode.getMessage());
        return ResponseEntity.status(status)
                .body(ApiResponse.fail(status, errorCode.getCode(), message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        // 모든 필드 오류 메시지를 결합
        String combined = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ", ErrorCode.BAD_REQUEST.getMessage() + ": ", ""));
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, combined);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(AuthenticationCredentialsNotFoundException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFound(UserNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Unhandled exception", e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
