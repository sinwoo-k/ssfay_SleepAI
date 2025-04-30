package com.c208.sleephony.domain.sleep.repositroy;

import com.c208.sleephony.domain.sleep.entity.BioData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BioRepository extends JpaRepository<BioData, Integer> {
}
