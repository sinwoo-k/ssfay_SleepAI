package com.c208.sleephony.domain.sleep.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class SummaryResponse {
    private String period;
    private Integer    averageSleepScore;
    private Integer    averageSleepTimeMinutes;
    private Integer    averageSleepLatencyMinutes;
    private Integer    averageLightSleepMinutes;
    private Integer    averageLightSleepPercentage;
    private Integer    averageRemSleepMinutes;
    private Integer    averageRemSleepPercentage;
    private Integer    averageDeepSleepMinutes;
    private Integer    averageDeepSleepPercentage;
    private Integer    averageAwakeMinutes;
    private Integer    averageAwakePercentage;
    private Integer    averageSleepCycleCount;

}
