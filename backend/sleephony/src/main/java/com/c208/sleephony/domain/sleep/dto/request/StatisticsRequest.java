package com.c208.sleephony.domain.sleep.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class StatisticsRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    private PeriodType periodType;

    public enum PeriodType {
        WEEK, MONTH, YEAR
    }
}
