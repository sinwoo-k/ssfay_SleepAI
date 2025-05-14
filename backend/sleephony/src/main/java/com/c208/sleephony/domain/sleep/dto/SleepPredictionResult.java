package com.c208.sleephony.domain.sleep.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SleepPredictionResult {
    private String requestId;
    private List<String> level;

}
