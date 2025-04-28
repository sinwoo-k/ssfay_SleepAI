package com.c208.sleephony.domain.authentication.contoller;

import com.c208.sleephony.domain.authentication.dto.response.LoginResponse;
import com.c208.sleephony.domain.authentication.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String,String> request) {
        LoginResponse response = authenticationService.loginWithGoogle(request);
        return ResponseEntity.ok(response);
    }

}
