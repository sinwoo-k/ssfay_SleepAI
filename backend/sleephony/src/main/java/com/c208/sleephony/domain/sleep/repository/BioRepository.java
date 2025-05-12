package com.c208.sleephony.domain.sleep.repository;

import com.c208.sleephony.domain.sleep.entity.BioData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BioRepository extends JpaRepository<BioData, Integer> {
    List<BioData> findByUserIdAndMeasuredAtBetween(Integer userId, LocalDateTime start, LocalDateTime end);

}
