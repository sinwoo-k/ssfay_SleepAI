package com.c208.sleephony.domain.sleep.service;

import com.c208.sleephony.domain.sleep.dto.request.RawSequenceKafkaPayload;
import com.c208.sleephony.domain.sleep.dto.request.RawSequenceRequest;
import com.c208.sleephony.domain.sleep.dto.response.RawSequenceResponse;
import com.c208.sleephony.domain.sleep.entity.SleepLevel;
import com.c208.sleephony.domain.sleep.entity.SleepStage;
import com.c208.sleephony.domain.sleep.repository.SleepLevelRepository;
import com.c208.sleephony.domain.sleep.utils.SleepStageFilter;
import com.c208.sleephony.global.exception.RedisOperationException;
import com.c208.sleephony.global.utils.AuthUtil;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SleepMeasurementService {

    private final SleepLevelRepository sleepLevelRepository;
    private final KafkaTemplate<String, RawSequenceKafkaPayload> kafkaTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> startTimeMap = new ConcurrentHashMap<>();
    private final SleepStageFilter stageFilter = new SleepStageFilter();

    @Value("${sleep.kafka.request-topic:sleep-raw-stage-request}")
    private String requestTopic;

    /**
     * RawSequenceRequest를 Kafka로 전송하여 수면 단계 추론을 시작하고,
     * 결과를 SseEmitter로 클라이언트에 스트리밍합니다.
     *
     * @param req RawSequenceRequest 객체, 센서 원시 데이터 포함
     * @return 클라이언트로 이벤트를 전송할 SseEmitter
     * @throws IllegalArgumentException HR 데이터가 없거나 비어 있을 경우 발생
     */
    public SseEmitter streamRawSleepStage(RawSequenceRequest req) {
        String requestId = UUID.randomUUID().toString();
        Integer userId = AuthUtil.getLoginUserId();
        log.info("[Request Data Sizes] userId={}, requestId={}, accX_size={}, accY_size={}, accZ_size={}, temp_size={}, hr_size={}",
                userId, requestId,
                req.getAccX().size(),
                req.getAccY().size(),
                req.getAccZ().size(),
                req.getTemp().size(),
                req.getHr().size());
        SseEmitter emitter = new SseEmitter(TimeUnit.SECONDS.toMillis(60));
        emitter.onCompletion(() -> emitters.remove(requestId));
        emitter.onTimeout(()    -> emitters.remove(requestId));
        emitters.put(requestId, emitter);

        startTimeMap.put(requestId, req.getMeasuredAt());
        List<Float> hrFixed = forwardFillZerosHr(req.getHr());   // ← HR만 보정

        RawSequenceKafkaPayload payload = new RawSequenceKafkaPayload(
                req.getAccX(), req.getAccY(), req.getAccZ(),
                req.getTemp(), hrFixed);
        // 보정된 HR 데이터도 로깅
        log.info("[HR Fixed] userId={}, requestId={}, hr_fixed_size={}, zeros_replaced={}, hr_fixed_values={}",
                userId, requestId, hrFixed.size(),
                req.getHr().stream().filter(hr -> hr == 0.0f).count(),
                hrFixed.subList(0, Math.min(10, hrFixed.size())) + "...");
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
     * Kafka로부터 수면 단계 응답(라벨)을 수신하여 DB에 저장한 뒤,
     * 해당 요청 ID에 매핑된 SseEmitter로 결과를 전송합니다.
     *
     * @param record     Kafka ConsumerRecord, RawSequenceResponse 포함
     * @param requestId  요청 식별용 헤더(requestId)
     * @param userIdStr  사용자 ID 헤더 문자열
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
        LocalDateTime measuredAt = startTimeMap.remove(requestId);

        RawSequenceResponse res = record.value();

        List<String> raw = res.getLabels();
        int needPad = Math.max(0, 5 - raw.size());
        List<SleepStage> lastFive = new ArrayList<>(5);
        for (int i = 0; i < needPad; i++) lastFive.add(SleepStage.AWAKE);   // padding
        raw.stream()
                .skip(Math.max(0, raw.size() - 5))            // 뒤에서 5개
                .map(l -> STAGE_MAP.getOrDefault(l, SleepStage.AWAKE))
                .forEach(lastFive::add);


        SleepStage stage = stageFilter.filter(lastFive);

        String redisKey = "sleep:start:" + userId;
        String startedAtStr = stringRedisTemplate.opsForValue().get(redisKey);
        if (startedAtStr != null) {
            LocalDateTime startedAt = LocalDateTime.parse(startedAtStr);
            if (Duration.between(startedAt, measuredAt).getSeconds() <= 600) {
                log.info("[SleepStage Override] First 10 minutes → Forcing AWAKE (was: {})", stage);
                stage = SleepStage.AWAKE;
            }
        }

        SleepLevel entity = SleepLevel.builder()
                .userId(userId)
                .level(stage)
                .measuredAt(measuredAt)
                .createdAt(LocalDateTime.now())
                .build();

        sleepLevelRepository.save(entity);
        log.info("[Kafka] user {} – filtered={} / raw={} @ {}",
                userId, stage, raw, measuredAt);

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
     * 수면 측정 시작 시각을 Redis에 24시간 TTL로 저장합니다.
     *
     * @param startedAt 측정 시작 시각
     * @return 저장 완료 메시지
     * @throws RedisOperationException Redis 저장 실패 시 발생
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

    private static final Map<String, SleepStage> STAGE_MAP = Map.of(
            "W",  SleepStage.AWAKE,
            "R",  SleepStage.REM,
            "N1", SleepStage.NREM1,
            "N2", SleepStage.NREM2,
            "N3", SleepStage.NREM3
    );
    public static List<Float> forwardFillZerosHr(List<Float> hr) {
        int n = hr.size();
        List<Float> filled = new ArrayList<>(n);
        Float last = null;

        /* 1차 : forward-fill (앞 값 복사) */
        for (Float v : hr) {
            if (v != null && v != 0f) {
                last = v;
                filled.add(v);
            } else {
                filled.add(last);           // last 가 null 일 수도!
            }
        }

        /* 2차 : back-fill (맨 첫 구간이 null 이면 뒷 값으로) */
        if (filled.get(0) == null) {
            // 뒤에서 첫 정상값 찾기
            Float next = null;
            for (Float v : filled) {
                if (v != null) { next = v; break; }
            }
            if (next == null) {
                throw new IllegalArgumentException("HR list is all zeros/nulls");
            }
            for (int i = 0; i < n && filled.get(i) == null; i++) {
                filled.set(i, next);
            }
        }

        /* null 은 더 이상 없음 → 0 으로 남겨둘지 NaN 으로 바꿀지 선택 */
        for (int i = 0; i < n; i++) {
            if (filled.get(i) == null) {
                // 실무에서는 0f 보다는 NaN 이 후단 처리에 안전
                filled.set(i, Float.NaN);
            }
        }
        return filled;
    }
}
