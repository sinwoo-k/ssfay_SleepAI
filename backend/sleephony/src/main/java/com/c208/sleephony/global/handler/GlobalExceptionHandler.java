package com.c208.sleephony.global.handler;

import com.c208.sleephony.global.exception.*;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;
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

    /* ---------- 공통 JSON 응답 ---------- */
    private ResponseEntity<ApiResponse<?>> jsonError(HttpStatus status,
                                                     ErrorCode code,
                                                     String message) {
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.fail(status, code.getCode(),
                        message != null ? message : code.getMessage()));
    }
    private ResponseEntity<String> sseError(HttpStatus status,
                                            ErrorCode code,
                                            String message) {

        // SSE 규격 :  event: <name>\n data: <payload>\n\n
        String payload = String.format(
                "event: error\ndata: {\"code\":\"%s\",\"message\":\"%s\"}\n\n",
                code.getCode(),
                message != null ? message : code.getMessage()
        );

        return ResponseEntity
                .status(status)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(payload);
    }
    private boolean isSseRequest(HttpServletRequest request) {
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        return accept != null && accept.contains(MediaType.TEXT_EVENT_STREAM_VALUE);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        // 모든 필드 오류 메시지를 결합
        String combined = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ", ErrorCode.BAD_REQUEST.getMessage() + ": ", ""));
        return jsonError(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, combined);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorized(AuthenticationCredentialsNotFoundException e) {
        return jsonError(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException e) {
        return jsonError(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFound(UserNotFoundException e) {
        return jsonError(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Unhandled exception", e);
        return jsonError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ThemeNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleThemeNotFound(ThemeNotFoundException e) {
        return jsonError(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(SleepReportNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleSleepReportNotFound(SleepReportNotFoundException e) {
        return jsonError(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidDate(InvalidDateRangeException e) {
        return jsonError(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({SleepPredictionException.class, SleepReportGenerationException.class})
    public ResponseEntity<ApiResponse<?>> handleReportError(RuntimeException e) {
        return jsonError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
                "수면 리포트 생성 중 오류가 발생했습니다.");
    }

    @ExceptionHandler(GptApiException.class)
    public ResponseEntity<ApiResponse<?>> handleGptError(GptApiException e) {
        return jsonError(HttpStatus.BAD_GATEWAY, ErrorCode.INTERNAL_SERVER_ERROR,
                "AI 분석 서버와의 통신에 실패했습니다.");
    }

    @ExceptionHandler(RedisOperationException.class)
    public ResponseEntity<ApiResponse<?>> handleRedisError(RedisOperationException e) {
        return jsonError(HttpStatus.SERVICE_UNAVAILABLE, ErrorCode.INTERNAL_SERVER_ERROR,
                "데이터 저장소 연결에 문제가 발생했습니다.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String param = e.getName();
        String value = e.getValue() == null ? "" : e.getValue().toString();
        String msg = String.format("'%s' 파라미터의 값 '%s' 형식이 올바르지 않습니다. 올바른 날짜 포맷은 yyyy-MM-dd 입니다.", param, value);
        return jsonError(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, msg);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidMonthFormat(DateTimeParseException e) {
        String msg = "month 파라미터의 형식이 올바르지 않습니다. 올바른 포맷은 yyyy-MM 입니다.";
        return jsonError(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, msg);
    }

    @ExceptionHandler({ NoHandlerFoundException.class,   // URL 자체를 못 찾음
            NoResourceFoundException.class })// 정적 리소스 fallback 도 못 찾음
    public ResponseEntity<ApiResponse<String>> handleNotFound(Exception ex) {
        log.warn("404 Not Found : {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(
                        HttpStatus.NOT_FOUND,
                        "요청하신 리소스를 찾을 수 없습니다."
                ));
    }
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<?> handleAsyncTimeout(AsyncRequestTimeoutException ex,
                                                HttpServletRequest req) {

        log.warn("Async request timed out: {}", ex.getMessage());
        return isSseRequest(req)
                ? sseError(HttpStatus.SERVICE_UNAVAILABLE,
                ErrorCode.INTERNAL_SERVER_ERROR,
                "요청 처리 시간이 초과되었습니다. 잠시 후 다시 시도해주세요.")
                : jsonError(HttpStatus.SERVICE_UNAVAILABLE,
                ErrorCode.INTERNAL_SERVER_ERROR,
                "요청 처리 시간이 초과되었습니다. 잠시 후 다시 시도해주세요.");
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException ex) {
        String msg = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return jsonError(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST, msg);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiResponse<?>> handleNotAcceptable(
            HttpMediaTypeNotAcceptableException ex) {

        log.warn("406 Not Acceptable : {}", ex.getMessage());
        return jsonError(
                HttpStatus.NOT_ACCEPTABLE,
                ErrorCode.BAD_REQUEST,
                "지원하지 않는 응답 형식입니다."
        );
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex,
                                              HttpServletRequest req) {
        // BAD_REQUEST(400), code="BR", 메시지는 ex.getMessage()
        if (isSseRequest(req)) {
            return sseError(HttpStatus.BAD_REQUEST,
                    ErrorCode.BAD_REQUEST,
                    ex.getMessage());
        }
        return jsonError(HttpStatus.BAD_REQUEST,
                ErrorCode.BAD_REQUEST,
                ex.getMessage());
    }
}
