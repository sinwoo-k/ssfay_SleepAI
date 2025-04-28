package com.c208.sleephony.domain.user.dto.request;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserProfileRequest {
    private String nickname;
    private BigDecimal height;
    private BigDecimal weight;
    private LocalDate birthDate;
    private String gender;
}
