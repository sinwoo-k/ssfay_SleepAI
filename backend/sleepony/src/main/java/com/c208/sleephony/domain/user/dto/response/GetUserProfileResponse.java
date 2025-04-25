package com.c208.sleephony.domain.user.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GetUserProfileResponse {
    private String email;
    private String nickname;
    private BigDecimal height;
    private BigDecimal weight;
    private LocalDate birthDate;
    private String gender;
}
