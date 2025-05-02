package com.c208.sleephony.domain.sleep.service;

import com.c208.sleephony.domain.sleep.entity.SleepLevel;
import com.c208.sleephony.domain.sleep.entity.SleepReport;
import com.c208.sleephony.domain.sleep.repositroy.SleepLevelRepository;
import com.c208.sleephony.domain.sleep.repositroy.SleepReportRepository;
import com.c208.sleephony.global.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SleepReportService {
    private final SleepLevelRepository levelRepository;
    private final SleepReportRepository reportRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public SleepReport generateSleepReport(LocalDateTime endedAt) {
        Integer userId = AuthUtil.getLoginUserId();
        String key = "sleep:start:" + userId;
        LocalDateTime startedAt = LocalDateTime.parse(Objects.requireNonNull(stringRedisTemplate.opsForValue().getAndDelete(key)));
        List<SleepLevel> levels = levelRepository.findAllByUserIdAndMeasuredAtBetween(userId,startedAt,endedAt);

        int awake = 0, rem = 0, n1 = 0, n2 = 0, n3 = 0;
        LocalDateTime firstN1 = null;
        for(SleepLevel level : levels) {
            switch(level.getLevel()) {
                case "AWAKE" -> awake++;
                case "REM" -> rem++;
                case "NREM1" -> {
                    n1++; firstN1 = (firstN1 == null) ? level.getMeasuredAt() : firstN1;
                }
                case "NREM2" -> n2++;
                case "NREM3" -> n3++;
            }
        }

        int nrem = n1 + n2;
        int deep = n3;
        int cycle = Math.max(1, (rem + nrem + deep) / 90);

        int score = 100 - (int)(awake * 0.5) + (int)(rem * 0.2) + (int)(deep * 0.3);
        score = Math.max(1, Math.min(score, 100));

        SleepReport report = SleepReport.builder()
                .userId(userId)
                .sleepScore(score)
                .realSleepTime(firstN1)
                .sleepTime(startedAt)
                .sleepWakeTime(endedAt)
                .awakeTime(awake)
                .remTime(rem)
                .nremTime(nrem)
                .deepTime(deep)
                .sleepCycle(cycle)
                .build();
        return reportRepository.save(report);
    }

    public SleepReport getReportByDate(LocalDate date) {
        Integer userId = AuthUtil.getLoginUserId();
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.plusDays(1).atStartOfDay();

        return reportRepository
                .findFirstByUserIdAndSleepTimeBetween(userId, from, to)
                .orElse(null);
    }
}
