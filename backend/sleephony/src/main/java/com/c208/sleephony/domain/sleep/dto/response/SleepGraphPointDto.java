package com.c208.sleephony.domain.sleep.dto.response;

import com.c208.sleephony.domain.sleep.entity.SleepStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SleepGraphPointDto {
    private LocalDateTime measuredAt;
    private SleepStage level;
    private Integer score;
}
