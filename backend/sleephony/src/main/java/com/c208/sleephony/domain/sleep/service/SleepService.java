package com.c208.sleephony.domain.sleep.service;

import com.c208.sleephony.domain.sleep.dto.request.RawSequenceKafkaPayload;
import com.c208.sleephony.domain.sleep.dto.request.RawSequenceRequest;
import com.c208.sleephony.domain.sleep.dto.request.StatisticsRequest;
import com.c208.sleephony.domain.sleep.dto.response.*;
import com.c208.sleephony.domain.sleep.entity.*;
import com.c208.sleephony.domain.sleep.repository.SleepLevelRepository;
import com.c208.sleephony.domain.sleep.repository.SleepReportRepository;
import com.c208.sleephony.domain.sleep.repository.SleepStatisticRepository;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.c208.sleephony.global.exception.RedisOperationException;
import com.c208.sleephony.global.exception.SleepReportNotFoundException;
import com.c208.sleephony.global.exception.UserNotFoundException;
import com.c208.sleephony.global.utils.AuthUtil;
import com.c208.sleephony.global.utils.GptClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SleepService {

    private final SleepLevelRepository sleepLevelRepository;
    private final SleepReportRepository sleepReportRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final GptClient gptClient;
    private final UserRepository userRepository;
    private final SleepStatisticRepository sleepStatisticRepository;
    private final KafkaTemplate<String, RawSequenceKafkaPayload> kafkaTemplate;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> startTimeMap = new ConcurrentHashMap<>();

    @Value("${sleep.kafka.request-topic:sleep-stage-request}")
    private String requestTopic;

    public SseEmitter streamRawSleepStage(RawSequenceRequest req) {
        String requestId = UUID.randomUUID().toString();
        Integer userId = AuthUtil.getLoginUserId();

        SseEmitter emitter = new SseEmitter(TimeUnit.SECONDS.toMillis(60));
        emitter.onCompletion(() -> emitters.remove(requestId));
        emitter.onTimeout(()    -> emitters.remove(requestId));
        emitters.put(requestId, emitter);

        startTimeMap.put(requestId, req.getMeasuredAt());

        RawSequenceKafkaPayload payload = new RawSequenceKafkaPayload(
                req.getAccX(), req.getAccY(), req.getAccZ(),
                req.getTemp(), req.getHr());

        ProducerRecord<String, RawSequenceKafkaPayload> record =
                new ProducerRecord<>(requestTopic, String.valueOf(userId), payload);

        // 필수 헤더 추가
        record.headers()
                .add(new RecordHeader("requestId", requestId.getBytes(StandardCharsets.UTF_8)))
                .add(new RecordHeader("userId",    String.valueOf(userId).getBytes(StandardCharsets.UTF_8)));
        kafkaTemplate.send(record);
        return emitter;
    }

    /**
     * Kafka 응답 토픽 수신 → 해당 requestId의 Emitter로 전송 → complete()
     */
    @KafkaListener(
            topics = "${sleep.kafka.response-topic}",
            containerFactory = "rawKafkaListenerContainerFactory"
    )
    public void onSleepStageResponse(
            ConsumerRecord<String, RawSequenceResponse> record,
            @Header("requestId") String requestId,
            @Header("userId")    String userIdStr
    ) {
        Integer userId = Integer.parseInt(userIdStr);
        LocalDateTime clientMeasuredAt = startTimeMap.remove(requestId);

        RawSequenceResponse res = record.value();

        String rawLabel = res.getLabels().isEmpty()
                ? "W"                // 못 받으면 AWAKE 취급
                : res.getLabels().get(res.getLabels().size() - 1);

        SleepStage stage = STAGE_MAP.getOrDefault(rawLabel, SleepStage.AWAKE);

        SleepLevel entity = SleepLevel.builder()
                .userId(userId)
                .level(stage)
                .measuredAt(clientMeasuredAt)
                .createdAt(LocalDateTime.now())
                .build();
        sleepLevelRepository.save(entity);
        log.info("[Kafka] user {} – level {} @ {} saved",
                userId, res.getLabels(), clientMeasuredAt);

        SseEmitter emitter = emitters.remove(requestId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(requestId)
                        .name("sleepStage")
                        .data(stage));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }
    }




    /**
     * 측정 시작 시각을 24h TTL 로 Redis(`sleep:start:{userId}`)에 기록합니다.
     */
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
    /**
     * 측정 종료 → Redis 시작시각을 꺼내 서머리 계산 → SleepReport 저장.
     */
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
    /**
     * <b>특정 일자</b>의 리포트와 전일(있는 경우) 총 수면시간을 함께 반환합니다.
     */
    public SleepReportWithPrevious getReportByDate(LocalDate date) {
        Integer userId = AuthUtil.getLoginUserId();
        LocalDateTime todayFrom = date.atStartOfDay();
        LocalDateTime todayTo = date.plusDays(1).atStartOfDay();

        LocalDateTime prevFrom = date.minusDays(1).atStartOfDay();
        LocalDateTime prevTo   = date.atStartOfDay();

        SleepReport todayReport = sleepReportRepository
                .findFirstByUserIdAndSleepTimeBetween(userId, todayFrom, todayTo)
                .orElseThrow(() -> new SleepReportNotFoundException("해당 날짜 리포트가 없습니다."));

        SleepReport prevReport = sleepReportRepository
                .findFirstByUserIdAndSleepTimeBetween(userId, prevFrom, prevTo)
                .orElse(null);
        int prevTotalSleep = prevReport != null
                ? toMinutes(prevReport.getSleepTime(), prevReport.getSleepWakeTime())
                : 0;
        return SleepReportWithPrevious.builder()
                .todayReport(todayReport)
                .previousTotalSleepMinutes(prevTotalSleep)
                .build();
    }
    /**
     * 일자별 수면 단계 그래프용 원본 점(TimePoint)을 반환합니다.
     */
    public List<SleepGraphPoint> getSleepGraphPoints(LocalDate date) {

        Integer userId      = AuthUtil.getLoginUserId();
        LocalDateTime begin = date.atStartOfDay();          // 00:00:00(포함)
        LocalDateTime end   = date.plusDays(1).atStartOfDay(); // 다음 날 00:00:00(제외)

        List<SleepLevel> stored = sleepLevelRepository
                .findByUserIdAndMeasuredAtBetween(userId, begin, end.minusSeconds(1));

        Map<LocalDateTime, SleepStage> map = stored.stream()
                .collect(Collectors.toMap(SleepLevel::getMeasuredAt, SleepLevel::getLevel));

        List<SleepGraphPoint> result = new ArrayList<>(2880);

        for (LocalDateTime t = begin; t.isBefore(end); t = t.plusSeconds(30)) {
            SleepStage stage = map.getOrDefault(t, SleepStage.AWAKE);
            result.add(SleepGraphPoint.builder()
                    .measuredAt(t)
                    .level(stage)
                    .build());
        }
        return result;
    }

    public DetailedSleepReportResponse getDetailedReport(LocalDate date) {

        Integer userId = AuthUtil.getLoginUserId();

        /* ── 1) 오늘 리포트 ─────────────────────────────────── */
        SleepReport report = sleepReportRepository
                .findFirstByUserIdAndSleepTimeBetween(
                        userId,
                        date.atStartOfDay(),
                        date.plusDays(1).atStartOfDay())
                .orElseThrow(() ->
                        new SleepReportNotFoundException("해당 날짜 리포트가 없습니다."));

        /* ── 2) 내 연령·성별 평균치 ─────────────────────────── */
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        String ageGroup = AuthUtil.toAgeGroup(
                AuthUtil.getLoginUserAge(user.getBirthDate()));
        SleepStatistics.Gender gender =
                SleepStatistics.Gender.valueOf(user.getGender());

        SleepStatistics stat = sleepStatisticRepository
                .findFirstByAgeGroupAndGender(ageGroup, gender);

        SleepStatBenchmark bench = SleepStatBenchmark.builder()
                .avgTotalSleepMinutes(stat != null ? stat.getSleepDurationMinutes() : 0)
                .avgDeepSleepMinutes (stat != null ? stat.getDeepSleepMinutes()    : 0)
                .avgRemSleepMinutes  (stat != null ? stat.getRemSleepMinutes()     : 0)
                .build();

        /* 3) 파생 지표 -------------------------------------------------- */
        int totalMinutes = report.getDeepTime()
                + report.getNremTime()
                + report.getRemTime();

        int latencyMin = (int) Duration.between(
                report.getSleepTime(), report.getRealSleepTime()).toMinutes();

        /* 4) 점수 → 타이틀/설명 매핑 ------------------------------------ */
        int score = report.getSleepScore();
        String title, desc;
        if (score >= 85) {
            title = "회복의 잠";
            desc  = "깊은 수면과 렘수면이 이상적인 비율로 나타났어요. "
                    + "몸과 마음이 완전히 재충전된 최고의 수면입니다!";
        }
        else if (score >= 70) {
            title = "스트레스 해소의 잠";
            desc  = "렘수면 비율이 높아 두뇌 회복과 창의력 향상에 도움이 된 수면이에요. "
                    + "오늘 하루도 가벼운 마음으로 시작해 보세요!";
        }
        else if (score >= 55) {
            title = "조금 아쉬운 잠";
            desc  = "얕은 수면이 길어 깊은 회복이 부족했어요. "
                    + "취침 시간·환경을 조정해 깊은 수면을 늘려보세요.";
        }
        else {
            title = "뒤척인 잠";
            desc  = "깨어 있는 시간이 많아 수면 효율이 크게 떨어졌습니다. "
                    + "규칙적인 생활과 이완 루틴으로 숙면을 준비해 보세요.";
        }

        /* ── 5) DTO 빌드 & 반환 ─────────────────────────────── */
        return DetailedSleepReportResponse.builder()
                .sleepScore(score)
                .title(title)
                .description(desc)
                .sleepStart(report.getSleepTime())
                .sleepEnd(report.getSleepWakeTime())
                .totalSleepMinutes(totalMinutes)
                .sleepLatencyMinutes(latencyMin)
                .deepSleepMinutes(report.getDeepTime())
                .remSleepMinutes(report.getRemTime())
                .statistics(bench)
                .build();
    }
    /**
     * GPT 모델에 오늘 리포트를 보내 500자 이상 코칭 문구를 받아옵니다.
     *  – 하루 1 회 캐싱(Redis key : sleep:advice:{user}:{date})
     */
    public String advise(SleepReportWithPrevious report) {
        Integer userId = AuthUtil.getLoginUserId();
        SleepReport todayReport = report.getTodayReport();
        LocalDate reportDate = todayReport.getSleepTime().toLocalDate(); // 수면 시작 날짜 기준

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
                todayReport.getSleepScore(),
                toMinutes(todayReport.getSleepTime(), todayReport.getSleepWakeTime()),
                todayReport.getRemTime(),
                todayReport.getNremTime(),
                todayReport.getDeepTime(),
                todayReport.getAwakeTime(),
                todayReport.getSleepCycle());

        // WebClient 는 reactive 이므로 block() 으로 동기화
        String advice = gptClient.getAdvice(prompt).block();
        stringRedisTemplate.opsForValue().set(redisKey, Objects.requireNonNull(advice), Duration.ofDays(1));

        return advice;
    }

    /**
     * <h3>Summary API</h3>
     * <ul>
     *   <li>요청 기간의 리포트만 집계, <b>리포트가 있는 날 수(분모)</b>로 나눔</li>
     *   <li>동일 길이의 “이전 기간” 평균 수면시간도 함께 반환</li>
     * </ul>
     *
     * @return {@link SummaryResponse}
     */
    public SummaryResponse summarize(StatisticsRequest req) {
        Integer userId = AuthUtil.getLoginUserId();
        List<SleepReport> currentReports  = fetchReports(userId, req);

        if (currentReports .isEmpty()) {
            throw new SleepReportNotFoundException("해당 기간 중 기록된 수면 리포트가 없습니다.");
        }

        long days = ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1;
        LocalDate prevEndDate = req.getStartDate().minusDays(1);
        LocalDate prevStartDate = prevEndDate.minusDays(days - 1);
        StatisticsRequest prevReq = new StatisticsRequest(prevStartDate, prevEndDate, req.getPeriodType());
        List<SleepReport> previousReports = fetchReports(userId, prevReq);

        // LocalTime만 추출하여 평균 (결측치: getSleepWakeTime() == null 제거)
        List<LocalTime> wakeTimes = currentReports.stream()
                .map(SleepReport::getSleepWakeTime)
                .filter(Objects::nonNull)
                .map(LocalDateTime::toLocalTime)
                .toList();

        // 평균 시간 계산
        int wakeCount = wakeTimes.size();
        int totalWakeMinutes = wakeTimes.stream()
                .mapToInt(t -> t.getHour() * 60 + t.getMinute())
                .sum();

        String avgWakeTime = wakeCount == 0
                ? null
                : String.format("%02d:%02d", (totalWakeMinutes / wakeCount) / 60, (totalWakeMinutes / wakeCount) % 60);

        long prevDaysWithReport = previousReports.stream()
                .map(r -> r.getSleepTime().toLocalDate())
                .distinct()
                .count();
        long prevSumTime = previousReports.stream()
                .mapToLong(r ->
                        Duration.between(r.getSleepTime(), r.getSleepWakeTime()).toMinutes()
                )
                .sum();
        int prevAvgTime = prevDaysWithReport == 0
                ? 0
                : (int) Math.round((double) prevSumTime / prevDaysWithReport);

        // “리포트가 있는 날짜 수” 계산
        long daysWithReport = currentReports .stream()
                .map(r -> r.getSleepTime().toLocalDate())
                .distinct()
                .count();
        // 합계 계산
        double sumScore   = currentReports .stream().mapToInt(SleepReport::getSleepScore).sum();
        double sumTime    = currentReports .stream()
                .mapToLong(r -> Duration.between(r.getSleepTime(), r.getSleepWakeTime()).toMinutes())
                .sum();
        double sumLatency = currentReports .stream()
                .mapToLong(r -> Duration.between(r.getSleepTime(), r.getRealSleepTime()).toMinutes())
                .sum();
        double sumLight   = currentReports .stream().mapToInt(SleepReport::getNremTime).sum();
        double sumRem     = currentReports .stream().mapToInt(SleepReport::getRemTime).sum();
        double sumDeep    = currentReports .stream().mapToInt(SleepReport::getDeepTime).sum();
        double sumAwake   = currentReports .stream().mapToInt(SleepReport::getAwakeTime).sum();
        double sumCycles  = currentReports .stream().mapToInt(SleepReport::getSleepCycle).sum();

        // “리포트 있는 일수”로 나눈 평균
        int avgScore     = (int)(sumScore   / daysWithReport);
        int avgTime      = (int)(sumTime    / daysWithReport);
        int avgLatency   = (int)(sumLatency / daysWithReport);
        int avgLight     = (int)(sumLight   / daysWithReport);
        int avgRem       = (int)(sumRem     / daysWithReport);
        int avgDeep      = (int)(sumDeep    / daysWithReport);
        int avgAwake     = (int)(sumAwake   / daysWithReport);
        int avgCycles    = (int)(sumCycles  / daysWithReport);
        // 날짜별 총 수면시간(분) 계산
        Map<LocalDate, Long> sleepByDate = currentReports.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getSleepTime().toLocalDate(),
                        Collectors.summingLong(r ->
                                Duration.between(r.getSleepTime(), r.getSleepWakeTime()).toMinutes()
                        )
                ));

        // 최장/최단 수면 시간(분) 추출
        long maxMinutes = sleepByDate.values().stream().mapToLong(Long::longValue).max().orElse(0L);
        long minMinutes = sleepByDate.values().stream().mapToLong(Long::longValue).min().orElse(0L);

        return SummaryResponse.builder()
                .period(formatPeriod(req.getStartDate(), req.getEndDate()))
                .averageSleepScore(avgScore)
                .averageSleepTimeMinutes(avgTime)
                .previousAverageSleepTimeMinutes(prevAvgTime)
                .averageSleepLatencyMinutes(avgLatency)
                .averageLightSleepMinutes(avgLight)
                .averageLightSleepPercentage((int)percentage(avgLight, avgTime))
                .averageRemSleepMinutes(avgRem)
                .averageRemSleepPercentage((int)percentage(avgRem, avgTime))
                .averageDeepSleepMinutes(avgDeep)
                .averageDeepSleepPercentage((int)percentage(avgDeep, avgTime))
                .averageAwakeMinutes(avgAwake)
                .averageAwakePercentage((int)percentage(avgAwake, avgTime))
                .averageSleepCycleCount(avgCycles)
                .averageWakeUpTime(avgWakeTime)
                .mostSleepTimeMinutes((int) maxMinutes)
                .leastSleepTimeMinutes((int) minMinutes)
                .build();
    }
    /**
     * 요일 / 주차 / 월 단위로 그룹화한 평균치를 그래프용 시계열로 제공합니다.
     */
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


    /**
     * Summary + 연령·성별 평균치(사전 입력된 통계 테이블) 묶음 반환.
     */
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
                .toList();
    }

    private List<SleepReport> fetchReports(Integer userId, StatisticsRequest req){
        LocalDateTime startDate = req.getStartDate().atStartOfDay();
        LocalDateTime endDate = req.getEndDate().atStartOfDay();
        return sleepReportRepository.findByUserIdAndSleepTimeBetween(userId, startDate, endDate);
    }


    private int toMinutes(LocalDateTime from, LocalDateTime to) {
        return (int) java.time.Duration.between(from, to).toMinutes();
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
                .map(label -> {
                    List<Integer> vals = buckets.get(label);
                    int avg = (vals == null || vals.isEmpty())
                            ? 0
                            : (int) Math.round(vals.stream()
                            .mapToInt(Integer::intValue)
                            .average()
                            .orElse(0.0));
                    return new GraphResponse.TimePoint(label, avg);
                })
                .toList();
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

    private static final Map<String, SleepStage> STAGE_MAP = Map.of(
            "W",  SleepStage.AWAKE,
            "R",  SleepStage.REM,
            "N1", SleepStage.NREM1,
            "N2", SleepStage.NREM2,
            "N3", SleepStage.NREM3
    );
}
