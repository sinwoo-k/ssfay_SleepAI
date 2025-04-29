package com.c208.sleephony.domain.sleep.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BioDataResponseDto {
    private Integer status;
    private String message;
    private String code;
    private String sleepLevel;
}
