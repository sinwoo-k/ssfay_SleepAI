package com.c208.sleephony.domain.sleep.dto.response;

import com.c208.sleephony.domain.sleep.entity.SleepStatistics;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CombinedStatResponse {
    private SummaryResponse summary;
    private List<SleepStatistics> myStatistics;
}
