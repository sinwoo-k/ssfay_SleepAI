package com.c208.sleephony.domain.sleep.repository;

import com.c208.sleephony.domain.sleep.entity.SleepSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SleepSessionRepository extends JpaRepository<SleepSession, Long> {
    Optional<SleepSession> findFirstByUserIdOrderByCreatedAtDesc(Integer userId);
    void deleteByUserId(Integer userId);
}
