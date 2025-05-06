package com.c208.sleephony.domain.sleep.service;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.dto.response.SleepGraphPointDto;
import com.c208.sleephony.domain.sleep.entity.BioData;
import com.c208.sleephony.domain.sleep.entity.SleepLevel;
import com.c208.sleephony.domain.sleep.entity.SleepReport;
import com.c208.sleephony.domain.sleep.entity.SleepStage;
import com.c208.sleephony.domain.sleep.repositroy.BioRepository;
import com.c208.sleephony.domain.sleep.repositroy.SleepLevelRepository;
import com.c208.sleephony.domain.sleep.repositroy.SleepReportRepository;
import com.c208.sleephony.global.utils.AuthUtil;
import com.c208.sleephony.global.utils.GptClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class SleepService {

    private final BioRepository bioRepository;
    private final SleepLevelRepository sleepLevelRepository;
    private final SleepReportRepository sleepReportRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final GptClient gptClient;

    public List<SleepPredictionResult> measureSleepStage(BioDataRequestDto requestDto) {
        LocalDateTime baseTime = LocalDateTime.parse(requestDto.getMeasuredAt());
        Integer userId = AuthUtil.getLoginUserId();

        List<BioData> entities = requestDto.getData().stream()
                .map(dataPoint -> BioData.builder()
                        .userId(userId)
                        .heartRate(dataPoint.getHeartRate().byteValue())
                        .gyroX(dataPoint.getGyroX())
                        .gyroY(dataPoint.getGyroY())
                        .gyroZ(dataPoint.getGyroZ())
                        .bodyTemperature(0.0f)
                        .createdAt(LocalDateTime.now())
                        .measuredAt(baseTime)
                        .build())
                .toList();

        bioRepository.saveAll(entities);
        return predictAndSaveAll(entities);
    }

    public List<SleepPredictionResult> predictFromBioDataByDate(LocalDate date) {
        Integer userId = AuthUtil.getLoginUserId();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        List<BioData> dataList = bioRepository.findByUserIdAndMeasuredAtBetween(userId, start, end);
        return predictAndSaveAll(dataList);
    }

    public List<SleepPredictionResult> predictAndSaveAll(List<BioData> dataList) {
        List<SleepLevel> entities = new ArrayList<>();
        List<SleepPredictionResult> results = new ArrayList<>();

        for (BioData data : dataList) {
            SleepPredictionResult result = predict(data);

            SleepLevel level = SleepLevel.builder()
                    .userId(data.getUserId())
                    .level(SleepStage.valueOf(result.getLevel()))
                    .score(result.getScore())
                    .measuredAt(data.getMeasuredAt())
                    .createdAt(LocalDateTime.now())
                    .build();

            entities.add(level);
            results.add(result);
        }

        sleepLevelRepository.saveAll(entities);
        return results;
    }

    private SleepPredictionResult predict(BioData data) {
        double gyroMagnitude = Math.sqrt(
                Math.pow(data.getGyroX(), 2) +
                        Math.pow(data.getGyroY(), 2) +
                        Math.pow(data.getGyroZ(), 2)
        );

        String level;
        int score;

        if (gyroMagnitude > 4.0) {
            level = "AWAKE";
            score = random(5, 20);
        } else if (data.getHeartRate() < 60 && data.getBodyTemperature() < 36.0) {
            level = "NREM3";
            score = random(90, 100);
        } else if (data.getHeartRate() < 70) {
            level = "NREM2";
            score = random(70, 90);
        } else {
            level = "REM";
            score = random(60, 80);
        }

        return new SleepPredictionResult(level, score, data.getMeasuredAt());
    }

    private int random(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public String startMeasurement(LocalDateTime startedAt) {
        Integer userId = AuthUtil.getLoginUserId();
        String key = "sleep:start:" + userId;
        stringRedisTemplate.opsForValue().set(key, startedAt.toString(), Duration.ofHours(24));
        return "측정 시작 시각 저장 완료";
    }

    public SleepReport generateSleepReport(LocalDateTime endedAt) {
        Integer userId = AuthUtil.getLoginUserId();
        String key = "sleep:start:" + userId;
        LocalDateTime startedAt = LocalDateTime.parse(Objects.requireNonNull(stringRedisTemplate.opsForValue().getAndDelete(key)));
        List<SleepLevel> levels = sleepLevelRepository.findAllByUserIdAndMeasuredAtBetween(userId, startedAt, endedAt);

        int awake = 0, rem = 0, n1 = 0, n2 = 0, n3 = 0;
        LocalDateTime firstN1 = null;
        for (SleepLevel level : levels) {
            switch (level.getLevel()) {
                case AWAKE -> awake++;
                case REM -> rem++;
                case NREM1 -> {
                    n1++;
                    if (firstN1 == null) firstN1 = level.getMeasuredAt();
                }
                case NREM2 -> {
                    n2++;
                    if (firstN1 == null) firstN1 = level.getMeasuredAt();
                }
                case NREM3 -> n3++;
            }
        }

        int nrem = n1 + n2;
        int deep = n3;
        int cycle = Math.max(1, (rem + nrem + deep) / 90);
        int score = 100 - (int) (awake * 0.5) + (int) (rem * 0.2) + (int) (deep * 0.3);
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
                .createdAt(LocalDateTime.now())
                .build();

        return sleepReportRepository.save(report);
    }

    public SleepReport getReportByDate(LocalDate date) {
        Integer userId = AuthUtil.getLoginUserId();
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.plusDays(1).atStartOfDay();

        return sleepReportRepository.findFirstByUserIdAndSleepTimeBetween(userId, from, to).orElse(null);
    }

    public List<SleepGraphPointDto> getSleepGraphPoints(LocalDate date) {
        Integer userId = AuthUtil.getLoginUserId();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<SleepLevel> levels = sleepLevelRepository.findByUserIdAndMeasuredAtBetween(userId, start, end);

        return levels.stream()
                .map(level -> SleepGraphPointDto.builder()
                        .measuredAt(level.getMeasuredAt())
                        .level(level.getLevel())
                        .score(level.getScore())
                        .build())
                .toList();
    }

    public String advise(SleepReport report) {

        String prompt = String.format("""
                        당신은 숙면 코치입니다. 아래 사용자의 밤 수면 리포트를 보고
                        • 최대 2줄짜리 요약
                        • 한국어로 3개의 핵심 행동 팁(불릿 리스트)만 제시하세요.
                        
                        [수면 리포트]
                        - 수면 점수: %d/100
                        - 총 수면: %d분
                        - REM: %d분
                        - 얕은 수면(N1+N2): %d분
                        - 깊은 수면(N3): %d분
                        - 깨어 있음: %d분
                        - 수면 주기: %d회
                        """,
                report.getSleepScore(),
                toMinutes(report.getSleepTime(), report.getSleepWakeTime()),
                report.getRemTime(),
                report.getNremTime(),
                report.getDeepTime(),
                report.getAwakeTime(),
                report.getSleepCycle());

        // WebClient 는 reactive 이므로 block() 으로 동기화
        return gptClient.getAdvice(prompt).block();
    }

    private int toMinutes(LocalDateTime from, LocalDateTime to) {
        return (int) java.time.Duration.between(from, to).toMinutes();
    }

}
