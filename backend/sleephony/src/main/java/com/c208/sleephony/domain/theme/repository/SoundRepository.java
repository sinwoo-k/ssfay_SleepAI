package com.c208.sleephony.domain.theme.repository;

import com.c208.sleephony.domain.theme.entity.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Integer> {
    List<Sound> findByThemeId(Integer themeId);
}
