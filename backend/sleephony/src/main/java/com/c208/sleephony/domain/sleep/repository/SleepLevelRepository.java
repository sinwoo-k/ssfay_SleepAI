package com.c208.sleephony.domain.sleep.repository;

import com.c208.sleephony.domain.sleep.entity.SleepLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SleepLevelRepository extends JpaRepository<SleepLevel, Integer> {

    List<SleepLevel> findAllByUserIdAndMeasuredAtBetween(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    List<SleepLevel> findByUserIdAndMeasuredAtBetween(Integer userId, LocalDateTime start, LocalDateTime end);

}
