package com.c208.sleephony.domain.sleep.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class SleepPredictionResult {
    private String level;
    private int score;
    private LocalDateTime measuredAt;

}
