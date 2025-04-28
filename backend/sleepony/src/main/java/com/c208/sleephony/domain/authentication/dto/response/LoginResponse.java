package com.c208.sleephony.domain.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String status;
    private String accessToken;
}
