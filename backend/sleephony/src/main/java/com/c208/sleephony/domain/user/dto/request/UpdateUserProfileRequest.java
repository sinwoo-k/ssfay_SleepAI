package com.c208.sleephony.domain.user.dto.request;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateUserProfileRequest {
    private String nickname;
    private Float height;
    private Float weight;
    private LocalDate birthDate;
    private String gender;
}
