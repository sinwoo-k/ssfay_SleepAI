package com.c208.sleephony.domain.user.dto.response;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GetUserProfileResponse {
    private String email;
    private String nickname;
    private double height;
    private double weight;
    private LocalDate birthDate;
    private String gender;
}
