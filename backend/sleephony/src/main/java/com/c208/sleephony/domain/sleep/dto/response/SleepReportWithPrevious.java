package com.c208.sleephony.domain.sleep.dto.response;

import com.c208.sleephony.domain.sleep.entity.SleepReport;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class SleepReportWithPrevious {
    private final SleepReport todayReport;

    private final int previousTotalSleepMinutes;

}
