package com.c208.sleephony.domain.sleep.dto.response;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class GraphResponse {
    private List<TimePoint> sleepTime;
    private List<TimePoint> sleepLatency;
    private List<TimePoint> lightSleep;
    private List<TimePoint> remSleep;
    private List<TimePoint> deepSleep;
    private List<TimePoint> awakeTime;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimePoint {
        private String label;
        private int value;
    }
}
