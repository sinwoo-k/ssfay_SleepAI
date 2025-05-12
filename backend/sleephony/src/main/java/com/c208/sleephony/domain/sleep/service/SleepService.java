package com.c208.sleephony.domain.sleep.service;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequest;
import com.c208.sleephony.domain.sleep.dto.request.StatisticsRequest;
import com.c208.sleephony.domain.sleep.dto.response.CombinedStatResponse;
import com.c208.sleephony.domain.sleep.dto.response.GraphResponse;
import com.c208.sleephony.domain.sleep.dto.response.SleepGraphPoint;
import com.c208.sleephony.domain.sleep.dto.response.SummaryResponse;
import com.c208.sleephony.domain.sleep.entity.*;
import com.c208.sleephony.domain.sleep.repositroy.BioRepository;
import com.c208.sleephony.domain.sleep.repositroy.SleepLevelRepository;
import com.c208.sleephony.domain.sleep.repositroy.SleepReportRepository;
import com.c208.sleephony.domain.sleep.repositroy.SleepStatisticRepository;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.c208.sleephony.global.exception.RedisOperationException;
import com.c208.sleephony.global.exception.SleepPredictionException;
import com.c208.sleephony.global.exception.SleepReportNotFoundException;
import com.c208.sleephony.global.exception.UserNotFoundException;
import com.c208.sleephony.global.utils.AuthUtil;
import com.c208.sleephony.global.utils.GptClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class SleepService {

    private final BioRepository bioRepository;
    private final SleepLevelRepository sleepLevelRepository;
    private final SleepReportRepository sleepReportRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final GptClient gptClient;
    private final UserRepository userRepository;
    private final SleepStatisticRepository sleepStatisticRepository;

    public List<SleepPredictionResult> measureSleepStage(BioDataRequest requestDto) {
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
                            .bodyTemperature(dataPoint.getTemperature())
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

    public List<SleepPredictionResult> predictFromBioDataByRange(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        Integer userId = AuthUtil.getLoginUserId();
        // 1초 단위로 저장된 전체 리스트
        List<BioData> dataList =
                bioRepository.findByUserIdAndMeasuredAtBetween(userId, startDateTime, endDateTime);

        if (dataList.isEmpty()) {
            return Collections.emptyList();
        }

        // epoch 초로 변환 (UTC 기준)
        long startEpoch = startDateTime.toEpochSecond(ZoneOffset.UTC);
        long endEpoch   = endDateTime.toEpochSecond(ZoneOffset.UTC);
        final int WINDOW = 30; // 초 단위

        List<SleepLevel> entities = new ArrayList<>();
        List<SleepPredictionResult> results = new ArrayList<>();

        // startEpoch 부터 endEpoch 까지 WINDOW 간격으로 순회
        for (long windowStart = startEpoch;
             windowStart + WINDOW <= endEpoch;
             windowStart += WINDOW) {

            long windowEnd = windowStart + WINDOW;

            // 각 윈도우에 속하는 30개의 데이터만 필터링
            long finalWindowStart = windowStart;
            List<BioData> windowData = dataList.stream()
                    .filter(d -> {
                        long epoch = d.getMeasuredAt().toEpochSecond(ZoneOffset.UTC);
                        return epoch >= finalWindowStart && epoch < windowEnd;
                    })
                    .toList();

            if (windowData.isEmpty()) {
                continue;
            }

            // 윈도우 내 평균값 계산
            double avgHeartRate = windowData.stream()
                    .mapToInt(BioData::getHeartRate)
                    .average().orElse(0);

            double avgGyroX = windowData.stream()
                    .mapToDouble(BioData::getGyroX)
                    .average().orElse(0);
            double avgGyroY = windowData.stream()
                    .mapToDouble(BioData::getGyroY)
                    .average().orElse(0);
            double avgGyroZ = windowData.stream()
                    .mapToDouble(BioData::getGyroZ)
                    .average().orElse(0);

            double avgTemp = windowData.stream()
                    .mapToDouble(BioData::getBodyTemperature)
                    .average().orElse(0);

            // 평균값을 가지는 임시 BioData 객체 생성
            BioData aggregated = new BioData();
            aggregated.setUserId(userId);
            aggregated.setMeasuredAt(
                    LocalDateTime.ofEpochSecond(windowStart, 0, ZoneOffset.UTC)
            );
            aggregated.setHeartRate((byte) Math.round(avgHeartRate));
            aggregated.setGyroX((float)avgGyroX);
            aggregated.setGyroY((float)avgGyroY);
            aggregated.setGyroZ((float)avgGyroZ);
            aggregated.setBodyTemperature((float)avgTemp);

            // 예측 수행
            SleepPredictionResult result = predict(aggregated);

            // 엔티티로 변환
            entities.add(
                    SleepLevel.builder()
                            .userId(userId)
                            .level(SleepStage.valueOf(result.getLevel()))
                            .score(result.getScore())
                            .measuredAt(aggregated.getMeasuredAt())
                            .createdAt(LocalDateTime.now())
                            .build()
            );
            results.add(result);
        }

        // 한 번에 저장
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
        // Redis에서 시작 시각 조회
        LocalDateTime startedAt = LocalDateTime.parse(
                Objects.requireNonNull(stringRedisTemplate.opsForValue().getAndDelete(key))
        );

        // 30초 윈도우 단위로 저장된 SleepLevel 조회
        List<SleepLevel> levels = sleepLevelRepository
                .findAllByUserIdAndMeasuredAtBetween(userId, startedAt, endedAt);

        // 각 단계별 윈도우 카운트
        int awakeCount = 0, remCount = 0, n1Count = 0, n2Count = 0, n3Count = 0;
        LocalDateTime firstN1 = null;
        for (SleepLevel level : levels) {
            switch (level.getLevel()) {
                case AWAKE -> awakeCount++;
                case REM   -> remCount++;
                case NREM1 -> {
                    n1Count++;
                    if (firstN1 == null) firstN1 = level.getMeasuredAt();
                }
                case NREM2 -> {
                    n2Count++;
                    if (firstN1 == null) firstN1 = level.getMeasuredAt();
                }
                case NREM3 -> n3Count++;
            }
        }

        int nremCount = n1Count + n2Count;
        // 1 윈도우 = 30초 이므로, 초 단위로 환산
        int awakeSeconds = awakeCount * 30;
        int remSeconds   = remCount   * 30;
        int nremSeconds  = nremCount  * 30;
        int deepSeconds  = n3Count    * 30;

        // 분 단위로 변환 (반올림)
        int awakeMinutes = Math.toIntExact(Math.round(awakeSeconds / 60.0));
        int remMinutes   = Math.toIntExact(Math.round(remSeconds   / 60.0));
        int nremMinutes  = Math.toIntExact(Math.round(nremSeconds  / 60.0));
        int deepMinutes  = Math.toIntExact(Math.round(deepSeconds  / 60.0));

        // 수면 주기 계산 (90분당 1주기 가정)
        int totalSleepMinutes = (int) Duration.between(startedAt, endedAt).toMinutes();
        int cycleCount = Math.max(1, totalSleepMinutes / 90);

        // 최종 점수 계산 (예시 로직 그대로)
        int score = 100
                - (int)(awakeMinutes * 0.5)
                + (int)(remMinutes   * 0.2)
                + (int)(deepMinutes  * 0.3);
        score = Math.max(1, Math.min(score, 100));

        LocalDateTime realSleepTime = (firstN1 != null ? firstN1 : startedAt);

        // SleepReport 빌드
        SleepReport report = SleepReport.builder()
                .userId(userId)
                .sleepScore(score)
                .realSleepTime(realSleepTime)
                .sleepTime(startedAt)
                .sleepWakeTime(endedAt)
                .awakeTime(awakeMinutes)
                .remTime(remMinutes)
                .nremTime(nremMinutes)
                .deepTime(deepMinutes)
                .sleepCycle(cycleCount)
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

    public List<SleepGraphPoint> getSleepGraphPoints(LocalDate date) {
        Integer userId = AuthUtil.getLoginUserId();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<SleepLevel> levels = sleepLevelRepository.findByUserIdAndMeasuredAtBetween(userId, start, end);

        return levels.stream()
                .map(level -> SleepGraphPoint.builder()
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

    public SummaryResponse summarize(StatisticsRequest req) {
        Integer userId = AuthUtil.getLoginUserId();
        List<SleepReport> reports = fetchReports(userId, req);

        if (reports.isEmpty()) {
            throw new SleepReportNotFoundException("해당 기간 중 기록된 수면 리포트가 없습니다.");
        }

        // “리포트가 있는 날짜 수” 계산
        long daysWithReport = reports.stream()
                .map(r -> r.getSleepTime().toLocalDate())
                .distinct()
                .count();
        // 합계 계산
        double sumScore   = reports.stream().mapToInt(SleepReport::getSleepScore).sum();
        double sumTime    = reports.stream()
                .mapToLong(r -> Duration.between(r.getSleepTime(), r.getSleepWakeTime()).toMinutes())
                .sum();
        double sumLatency = reports.stream()
                .mapToLong(r -> Duration.between(r.getSleepTime(), r.getRealSleepTime()).toMinutes())
                .sum();
        double sumLight   = reports.stream().mapToInt(SleepReport::getNremTime).sum();
        double sumRem     = reports.stream().mapToInt(SleepReport::getRemTime).sum();
        double sumDeep    = reports.stream().mapToInt(SleepReport::getDeepTime).sum();
        double sumAwake   = reports.stream().mapToInt(SleepReport::getAwakeTime).sum();
        double sumCycles  = reports.stream().mapToInt(SleepReport::getSleepCycle).sum();

        // “리포트 있는 일수”로 나눈 평균
        float avgScore     = (float)(sumScore   / daysWithReport);
        float avgTime      = (float)(sumTime    / daysWithReport);
        float avgLatency   = (float)(sumLatency / daysWithReport);
        float avgLight     = (float)(sumLight   / daysWithReport);
        float avgRem       = (float)(sumRem     / daysWithReport);
        float avgDeep      = (float)(sumDeep    / daysWithReport);
        float avgAwake     = (float)(sumAwake   / daysWithReport);
        float avgCycles    = (float)(sumCycles  / daysWithReport);

        return SummaryResponse.builder()
                .period(formatPeriod(req.getStartDate(), req.getEndDate()))
                .averageSleepScore(avgScore)
                .averageSleepTimeMinutes(avgTime)
                .averageSleepLatencyMinutes(avgLatency)
                .averageLightSleepMinutes(avgLight)
                .averageLightSleepPercentage(percentage(avgLight, avgTime))
                .averageRemSleepMinutes(avgRem)
                .averageRemSleepPercentage(percentage(avgRem, avgTime))
                .averageDeepSleepMinutes(avgDeep)
                .averageDeepSleepPercentage(percentage(avgDeep, avgTime))
                .averageAwakeMinutes(avgAwake)
                .averageAwakePercentage(percentage(avgAwake, avgTime))
                .averageSleepCycleCount(avgCycles)
                .build();
    }

    public GraphResponse graph(StatisticsRequest req){
        Integer userId = AuthUtil.getLoginUserId();
        List<SleepReport> reports = fetchReports(userId, req);

        Function<SleepReport, Integer> sleepTimeFn = report ->
                (int) Duration.between(report.getSleepTime(), report.getSleepWakeTime()).toMinutes();
        Function<SleepReport, Integer> latencyFn = report ->
                (int) Duration.between(report.getSleepTime(), report.getRealSleepTime()).toMinutes();
        Function<SleepReport, Integer> deepTimeFn = SleepReport::getDeepTime;
        Function<SleepReport, Integer> awakeFn = SleepReport::getAwakeTime;
        Function<SleepReport, Integer> lightFn = SleepReport::getNremTime;
        Function<SleepReport, Integer> remFn = SleepReport::getRemTime;

        List<GraphResponse.TimePoint> sleepTime = aggregateAverage(reports, req.getPeriodType(), sleepTimeFn);
        List<GraphResponse.TimePoint> sleepLatency = aggregateAverage(reports, req.getPeriodType(), latencyFn);
        List<GraphResponse.TimePoint> deepTime = aggregateAverage(reports, req.getPeriodType(), deepTimeFn);
        List<GraphResponse.TimePoint> awakeTime = aggregateAverage(reports, req.getPeriodType(), awakeFn);
        List<GraphResponse.TimePoint> lights = aggregateAverage(reports, req.getPeriodType(), lightFn);
        List<GraphResponse.TimePoint> rems = aggregateAverage(reports, req.getPeriodType(), remFn);

        return GraphResponse.builder()
                .sleepTime(sleepTime)
                .sleepLatency(sleepLatency)
                .lightSleep(lights)
                .remSleep(rems)
                .deepSleep(deepTime)
                .awakeTime(awakeTime)
                .build();
    }

    private List<GraphResponse.TimePoint> aggregateAverage(
            List<SleepReport> reports,
            StatisticsRequest.PeriodType periodType,
            Function<SleepReport, Integer> valueFn
    ) {
        // 1) 레이블별 리스트 수집
        Map<String, List<Integer>> buckets = reports.stream()
                .collect(Collectors.groupingBy(
                        report -> makeLabel(report.getSleepTime(), periodType),
                        LinkedHashMap::new,
                        Collectors.mapping(valueFn, Collectors.toList())
                ));
        // 2) 정렬된 레이블 순서 확보
        List<String> allLabels = switch (periodType) {
            case WEEK -> List.of("월","화","수","목","금","토","일");
            case MONTH -> List.of("1주","2주","3주","4주","5주");
            case YEAR -> IntStream.rangeClosed(1,12)
                    .mapToObj(i -> i + "월")
                    .toList();
        };


        // 3) 평균으로 변환
        return allLabels.stream()
                .filter(buckets::containsKey)  // 결측 레이블(리포트 없는 주/월/년)은 건너뜀
                .map(label -> {
                    List<Integer> vals = buckets.get(label);
                    float avg = (float) vals.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0);
                    return new GraphResponse.TimePoint(label, avg);
                })
                .collect(Collectors.toList());
    }


    // 레이블 생성 (WEEK→요일, MONTH→몇 주차, YEAR→몇 월)
    private String makeLabel(LocalDateTime dt, StatisticsRequest.PeriodType type) {
        return switch (type) {
            case WEEK -> {
                DayOfWeek dow = dt.getDayOfWeek();
                yield dow.getDisplayName(TextStyle.SHORT, Locale.KOREAN);
            }
            case MONTH -> {
                int weekOfMonth = ((dt.getDayOfMonth() - 1) / 7) + 1;
                yield weekOfMonth + "주";
            }
            case YEAR -> dt.getMonthValue() + "월";
        };
    }
    public List<SleepReport> fetchReports(Integer userId, StatisticsRequest req){
        LocalDateTime startDate = req.getStartDate().atStartOfDay();
        LocalDateTime endDate = req.getEndDate().atStartOfDay();
        return sleepReportRepository.findByUserIdAndSleepTimeBetween(userId, startDate, endDate);
    }

    private String formatPeriod(java.time.LocalDate s, java.time.LocalDate e) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return fmt.format(s) + " ~ " + fmt.format(e);
    }

    private float percentage(double part, double total) {
        if (total <= 0) {
            return 0;
        }
        return (int) Math.round(part / total * 100);
    }

    public CombinedStatResponse getCombinedStats(StatisticsRequest req){
        SummaryResponse summary = summarize(req);

        Integer userId = AuthUtil.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        int age = AuthUtil.getLoginUserAge(user.getBirthDate());
        String ageGroup = AuthUtil.toAgeGroup(age);

        SleepStatistics.Gender genderEnum = SleepStatistics.Gender.valueOf(user.getGender());

        List<SleepStatistics> myStats =
                sleepStatisticRepository.findByAgeGroupAndGender(ageGroup, genderEnum);

        return CombinedStatResponse.builder()
                .summary(summary)
                .myStatistics(myStats)
                .build();
    }

    public List<LocalDate> getReportedDatesForMonth(String month){
        Integer userId = AuthUtil.getLoginUserId();
        YearMonth ym = YearMonth.parse(month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        List<SleepReport> reports = sleepReportRepository.findByUserIdAndSleepTimeBetween(userId,startDate,endDate);

        if (reports.isEmpty()) {
            throw new SleepReportNotFoundException("해당 월(" + month + ")에 등록된 수면 리포트가 없습니다.");
        }

        return reports.stream()
                .map(report -> report.getSleepTime().toLocalDate())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
