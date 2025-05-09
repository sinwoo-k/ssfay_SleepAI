package com.c208.sleephony.domain.sleep.repositroy;

import com.c208.sleephony.domain.sleep.entity.SleepReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SleepReportRepository extends JpaRepository<SleepReport, Integer> {
    Optional<SleepReport> findFirstByUserIdAndSleepTimeBetween(
            Integer userId,
            LocalDateTime from,
            LocalDateTime to
    );

    List<SleepReport> findByUserIdAndSleepTimeBetween(Integer userId, LocalDateTime startDate, LocalDateTime endDate);
}
