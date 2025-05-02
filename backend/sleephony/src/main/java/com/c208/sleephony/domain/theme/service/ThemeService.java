package com.c208.sleephony.domain.theme.service;

import com.c208.sleephony.domain.theme.dto.response.ThemeDetailResponse;
import com.c208.sleephony.domain.theme.entity.Sound;
import com.c208.sleephony.domain.theme.entity.Theme;
import com.c208.sleephony.domain.theme.repository.SoundRepository;
import com.c208.sleephony.domain.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final SoundRepository soundRepository;

    // 테마 전체 목록 조회
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    // 테마 상세 조회
    public ThemeDetailResponse getThemeDetail(Integer themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다. ID = " + themeId));

        List<Sound> sounds = soundRepository.findByThemeId(themeId);

        return ThemeDetailResponse.from(theme, sounds);
    }

}
