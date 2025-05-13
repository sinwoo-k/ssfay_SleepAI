package com.c208.sleephony.domain.sleep.repository;

import com.c208.sleephony.domain.sleep.entity.SleepStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SleepStatisticRepository extends JpaRepository<SleepStatistics,Integer> {
    List<SleepStatistics> findByAgeGroupAndGender(String ageGroup, SleepStatistics.Gender gender);
}
