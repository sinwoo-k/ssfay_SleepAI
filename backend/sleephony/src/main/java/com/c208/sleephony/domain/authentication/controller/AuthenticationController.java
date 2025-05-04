package com.c208.sleephony.domain.authentication.controller;

import com.c208.sleephony.domain.authentication.dto.response.LoginResponse;
import com.c208.sleephony.domain.authentication.service.AuthenticationService;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "인증 관리")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "구글 소셜 로그인")
    @PostMapping("/login-google")
    public ApiResponse<LoginResponse> loginWithGoogle(@RequestBody Map<String, String> request) {
        return ApiResponse.success(HttpStatus.OK,authenticationService.loginWithGoogle(request));
    }

    @Operation(summary = "카카오 소셜 로그인")
    @PostMapping("/login-kakao")
    public ApiResponse<LoginResponse> loginWithKakao(@RequestBody Map<String, String> request) {
        return ApiResponse.success(HttpStatus.OK,authenticationService.loginWithKakao(request));
    }

    @Operation(summary = "토큰 유효성 검사")
    @GetMapping("/validate")
    public ApiResponse<String> validate() {
        return ApiResponse.success(HttpStatus.OK, "유효한 토큰입니다.");
    }
}
