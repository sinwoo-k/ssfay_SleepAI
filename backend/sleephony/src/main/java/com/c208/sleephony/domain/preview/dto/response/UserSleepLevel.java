package com.c208.sleephony.domain.preview.dto.response;

import com.c208.sleephony.domain.sleep.entity.SleepLevel;
import com.c208.sleephony.domain.sleep.entity.SleepStage;

import java.time.LocalDateTime;

public record UserSleepLevel(LocalDateTime time, SleepStage level) {
    public static UserSleepLevel fromEntity(SleepLevel e) {
        return new UserSleepLevel(e.getMeasuredAt(), e.getLevel());
    }
}
