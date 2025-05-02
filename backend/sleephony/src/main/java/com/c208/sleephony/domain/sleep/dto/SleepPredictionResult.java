package com.c208.sleephony.domain.sleep.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SleepPredictionResult {
    private String level;
    private Integer score;
    private LocalDateTime measuredAt;

}
