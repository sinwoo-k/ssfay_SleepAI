package com.c208.sleephony.domain.authentication.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    @NotBlank
    private String status;

    @NotBlank
    private String accessToken;
}
