package com.c208.sleephony.domain.theme.repository;

import com.c208.sleephony.domain.theme.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Integer> {

}
