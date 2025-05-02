package com.c208.sleephony.domain.sleep.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class StartMeasurementRequestDto {

    private LocalDateTime startedAt;
}
