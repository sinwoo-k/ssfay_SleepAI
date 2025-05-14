package com.c208.sleephony.domain.sleep.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepStatBenchmark {
    private Integer avgTotalSleepMinutes;
    private Integer avgDeepSleepMinutes;
    private Integer avgRemSleepMinutes;
}