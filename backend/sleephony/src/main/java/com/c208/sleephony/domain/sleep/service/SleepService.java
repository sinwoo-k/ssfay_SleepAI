package com.c208.sleephony.domain.sleep.service;

import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.entity.BioData;
import com.c208.sleephony.domain.sleep.repositroy.BioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SleepService {

    private final BioRepository bioRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Transactional
    public void saveAll(BioDataRequestDto requestDto, Integer userId) {
        LocalDateTime baseTime = LocalDateTime.parse(requestDto.getMeasuredAt());

        List<BioData> entities = requestDto.getData().stream()
                .map(dataPoint -> BioData.builder()
                        .userId(userId)
                        .heartRate(dataPoint.getHeartRate().byteValue())
                        .gyroX(dataPoint.getGyroX())
                        .gyroY(dataPoint.getGyroY())
                        .gyroZ(dataPoint.getGyroZ())
                        .createdAt(LocalDateTime.now())
                        .bodyTemperature(Float.parseFloat("0.0"))
                        .measuredAt(baseTime)
                        .build()
                )
                .toList();
        bioRepository.saveAll(entities);
    }

    public void startMeasurement(Integer userId, LocalDateTime startedAt) {
        String key = "sleep:start:" + userId;
        stringRedisTemplate.opsForValue().setIfAbsent(key, startedAt.toString());
    }
}
