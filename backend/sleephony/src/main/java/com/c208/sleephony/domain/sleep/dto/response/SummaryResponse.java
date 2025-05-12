package com.c208.sleephony.domain.sleep.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class SummaryResponse {
    private String period;
    private Float     averageSleepScore;
    private Float     averageSleepTimeMinutes;
    private Float     averageSleepLatencyMinutes;
    private Float     averageLightSleepMinutes;
    private Float     averageLightSleepPercentage;
    private Float     averageRemSleepMinutes;
    private Float     averageRemSleepPercentage;
    private Float     averageDeepSleepMinutes;
    private Float     averageDeepSleepPercentage;
    private Float     averageAwakeMinutes;
    private Float     averageAwakePercentage;
    private Float     averageSleepCycleCount;

}
