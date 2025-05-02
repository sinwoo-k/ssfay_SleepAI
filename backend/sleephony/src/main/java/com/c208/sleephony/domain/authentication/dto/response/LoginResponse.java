package com.c208.sleephony.domain.authentication.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    @NotBlank
    private String status;

    @NotBlank
    private String accessToken;
}
