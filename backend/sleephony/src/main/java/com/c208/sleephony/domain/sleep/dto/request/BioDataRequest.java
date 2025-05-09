package com.c208.sleephony.domain.sleep.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BioDataRequest {
    private String measuredAt;
    private List<DataPoint> data;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DataPoint {
        private String time;
        private Integer heartRate;
        private Float gyroX;
        private Float gyroY;
        private Float gyroZ;
    }
}
