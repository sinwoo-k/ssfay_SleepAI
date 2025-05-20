package com.c208.sleephony.domain.preview.service;

import com.c208.sleephony.domain.sleep.repository.SleepLevelRepository;
import com.c208.sleephony.domain.preview.dto.response.UserSleepLevel;
import com.c208.sleephony.domain.preview.dto.response.UserSummaryDto;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.c208.sleephony.global.exception.RedisOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PreviewService {

    private final UserRepository userRepository;
    private final SleepLevelRepository sleepLevelRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String REDIS_KEY_PREFIX = "sleep:start:"; // 측정 중 여부·시작 시각 키

    public List<UserSummaryDto> getAllUsers() {
        return userRepository.findAllProjectedByDeleted('N');
    }

    public boolean isMeasuring(Integer userId) {
        String key = REDIS_KEY_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    public List<UserSleepLevel> getRecentSleepLevels(Integer userId) {
        String key = REDIS_KEY_PREFIX + userId;
        String startStr = redisTemplate.opsForValue().get(key);
        if (startStr == null) {
            throw new RedisOperationException("시작 시각이 존재하지 않습니다.");
        }
        LocalDateTime begin = LocalDateTime.parse(startStr);
        LocalDateTime now   = LocalDateTime.now();
        return sleepLevelRepository.findByUserIdAndMeasuredAtBetween(userId, begin, now)
                .stream()
                .map(UserSleepLevel::fromEntity)
                .toList();
    }
}
