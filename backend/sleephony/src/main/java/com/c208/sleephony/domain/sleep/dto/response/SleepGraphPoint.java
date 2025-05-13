package com.c208.sleephony.domain.sleep.dto.response;

import com.c208.sleephony.domain.sleep.entity.SleepStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SleepGraphPoint {
    private LocalDateTime measuredAt;
    private SleepStage level;
    private Integer score;
}
