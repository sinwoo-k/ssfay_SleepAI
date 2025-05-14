package com.c208.sleephony.domain.sleep.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedSleepReportResponse {
    private Integer       sleepScore;        // 수면 점수(1‑100)
    private String        title;             // “꿀잠”, “보통” …
    private String        description;       // 점수 기반 코멘트
    private LocalDateTime sleepStart;        // 수면 시작
    private LocalDateTime sleepEnd;          // 수면 종료

    private Integer totalSleepMinutes;           // 총 수면 (깨어있음 제외)
    private Integer sleepLatencyMinutes;         // 잠들기까지 걸린 시간
    private Integer deepSleepMinutes;            // 깊은 수면
    private Integer remSleepMinutes;             // REM 수면

    private SleepStatBenchmark statistics;

}
