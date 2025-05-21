package com.c208.sleephony.domain.preview.service;

import com.c208.sleephony.domain.sleep.entity.SleepSession;
import com.c208.sleephony.domain.sleep.repository.SleepLevelRepository;
import com.c208.sleephony.domain.preview.dto.response.UserSleepLevel;
import com.c208.sleephony.domain.preview.dto.response.UserSummaryDto;
import com.c208.sleephony.domain.sleep.repository.SleepSessionRepository;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import lombok.RequiredArgsConstructor;
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
    private final SleepSessionRepository sleepSessionRepository;

    public List<UserSummaryDto> getAllUsers() {
        return userRepository.findAllProjectedByDeleted('N');
    }

    public boolean isMeasuring(Integer userId) {
        return sleepSessionRepository
                .findFirstByUserIdOrderByCreatedAtDesc(userId)
                .isPresent();
    }

    public List<UserSleepLevel> getRecentSleepLevels(Integer userId) {
        SleepSession session = sleepSessionRepository
                .findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new IllegalStateException("측정 세션이 없습니다."));
        LocalDateTime begin = session.getStartedAt();
        LocalDateTime now   = LocalDateTime.now();
        return sleepLevelRepository
                .findByUserIdAndMeasuredAtBetween(userId, begin, now)
                .stream()
                .map(UserSleepLevel::fromEntity)
                .toList();
    }
}
