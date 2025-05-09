package com.c208.sleephony.domain.sleep.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class SummaryResponse {
    private String period;
    private int    averageSleepScore;
    private int    averageSleepTimeMinutes;
    private int    averageSleepLatencyMinutes;
    private int    averageLightSleepMinutes;
    private int    averageLightSleepPercentage;
    private int    averageRemSleepMinutes;
    private int    averageRemSleepPercentage;
    private int    averageDeepSleepMinutes;
    private int    averageDeepSleepPercentage;
    private int    averageAwakeMinutes;
    private int    averageAwakePercentage;
    private int    averageSleepCycleCount;

}
