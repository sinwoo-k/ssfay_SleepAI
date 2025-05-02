package com.c208.sleephony.domain.sleep.service;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.entity.BioData;
import com.c208.sleephony.domain.sleep.repositroy.BioRepository;
import com.c208.sleephony.global.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SleepService {

    private final BioRepository bioRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final SleepLevelService sleepLevelService;

    @Transactional
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
                        .bodyTemperature(0.0f) // 추후 필요시 입력
                        .createdAt(LocalDateTime.now())
                        .measuredAt(baseTime)
                        .build()
                )
                .toList();

        bioRepository.saveAll(entities);
        return sleepLevelService.predictAndSaveAll(entities);
    }

    public String startMeasurement(LocalDateTime startedAt) {
        Integer userId = AuthUtil.getLoginUserId();
        String key = "sleep:start:" + userId;
        String value = startedAt.toString();

        stringRedisTemplate.opsForValue().set(key, value, Duration.ofHours(24));
        return "측정 시작 시각 저장 완료";
    }
}
