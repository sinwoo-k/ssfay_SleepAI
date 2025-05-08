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
import com.c208.sleephony.global.exception.RedisOperationException;
import com.c208.sleephony.global.exception.SleepPredictionException;
import com.c208.sleephony.global.exception.SleepReportNotFoundException;
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
        try {
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
        } catch (Exception e) {
            throw new SleepPredictionException("수면 단계 예측 중 오류가 발생했습니다.", e);
        }
    }
    public List<SleepPredictionResult> predictAndSaveAll(List<BioData> dataList) {
        try {
            List<SleepLevel> entities = new ArrayList<>();
            List<SleepPredictionResult> results = new ArrayList<>();

            for (BioData data : dataList) {
                SleepPredictionResult result = predict(data);
                entities.add(
                        SleepLevel.builder()
                                .userId(data.getUserId())
                                .level(SleepStage.valueOf(result.getLevel()))
                                .score(result.getScore())
                                .measuredAt(data.getMeasuredAt())
                                .createdAt(LocalDateTime.now())
                                .build()
                );
                results.add(result);
            }

            sleepLevelRepository.saveAll(entities);
            return results;
        } catch (Exception e) {
            throw new SleepPredictionException("수면 단계 저장 또는 예측 중 오류가 발생했습니다.", e);
        }
    }
    public List<SleepPredictionResult> predictFromBioDataByDate(LocalDate date) {
        Integer userId = AuthUtil.getLoginUserId();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        List<BioData> dataList = bioRepository.findByUserIdAndMeasuredAtBetween(userId, start, end);
        return predictAndSaveAll(dataList);
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
        try {
            stringRedisTemplate.opsForValue().set(key, startedAt.toString(), Duration.ofHours(24));
            return "측정 시작 시각 저장 완료";
        } catch (Exception e) {
            throw new RedisOperationException("Redis에 측정 시작 시각 저장 중 오류가 발생했습니다.", e);
        }
    }

    public SleepReport generateSleepReport(LocalDateTime endedAt) {
        Integer userId = AuthUtil.getLoginUserId();
        String key = "sleep:start:" + userId;
        LocalDateTime startedAt;
        try {
            String raw = stringRedisTemplate.opsForValue().getAndDelete(key);
            startedAt = LocalDateTime.parse(Objects.requireNonNull(raw));
        } catch (Exception e) {
            throw new RedisOperationException("Redis에서 시작 시각을 조회/삭제하는 중 오류가 발생했습니다.", e);
        }
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

        return sleepReportRepository
                .findFirstByUserIdAndSleepTimeBetween(userId, from, to)
                .orElseThrow(() -> new SleepReportNotFoundException("해당 날짜 리포트가 없습니다."));
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
        Integer userId = AuthUtil.getLoginUserId();
        LocalDate reportDate = report.getSleepTime().toLocalDate(); // 수면 시작 날짜 기준

        String redisKey = "sleep:advice:" + userId + ":" + reportDate;
        String cachedAdvice = stringRedisTemplate.opsForValue().get(redisKey);

        // Redis에 값이 있으면 그대로 반환
        if (cachedAdvice != null) {
            return cachedAdvice;
        }
        String prompt = String.format("""
                        당신은 전문적인 수면 분석 AI 코치입니다. 아래 사용자의 수면 리포트를 기반으로, 최소 500자 이상의 상세한 피드백을 한국어로 작성해 주세요.
                        
                        [수면 리포트]
                        - 수면 점수: %d/100
                        - 총 수면 시간: %d분
                        - 렘수면(REM): %d분
                        - 얕은 수면(N1+N2): %d분
                        - 깊은 수면(N3): %d분
                        - 깨어 있는 시간: %d분
                        - 수면 주기 횟수: %d회
                        
                        요구사항:
                        1. 사용자의 수면 데이터를 해석하여 현재 수면 상태의 특징과 문제점을 설명하세요.
                        2. 각 수면 단계의 비율이 어떤 의미를 가지며, 건강한 수면과 비교해 어떤 부분이 부족하거나 개선될 수 있는지 구체적으로 설명하세요.
                        3. 수면 습관, 생활 패턴, 환경 등의 측면에서 개선해야 할 점을 구체적인 예시와 함께 조언하세요.
                        4. 수면 질을 향상시키기 위한 실천 가능한 조언을 최소 5가지 이상 제시하고, 각 조언의 이유도 설명하세요.
                        
                        최소 500자 이상으로 매우 구체적이고 실질적인 피드백을 제공하세요.
                        """,
                report.getSleepScore(),
                toMinutes(report.getSleepTime(), report.getSleepWakeTime()),
                report.getRemTime(),
                report.getNremTime(),
                report.getDeepTime(),
                report.getAwakeTime(),
                report.getSleepCycle());

        // WebClient 는 reactive 이므로 block() 으로 동기화
        String advice = gptClient.getAdvice(prompt).block();
        stringRedisTemplate.opsForValue().set(redisKey, Objects.requireNonNull(advice), Duration.ofDays(1));

        return advice;
    }

    private int toMinutes(LocalDateTime from, LocalDateTime to) {
        return (int) java.time.Duration.between(from, to).toMinutes();
    }

}
