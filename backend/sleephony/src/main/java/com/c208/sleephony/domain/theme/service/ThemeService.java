package com.c208.sleephony.domain.theme.service;

import com.c208.sleephony.domain.theme.dto.response.ThemeDetailResponse;
import com.c208.sleephony.domain.theme.entity.Sound;
import com.c208.sleephony.domain.theme.entity.Theme;
import com.c208.sleephony.domain.theme.repository.SoundRepository;
import com.c208.sleephony.domain.theme.repository.ThemeRepository;
import com.c208.sleephony.global.exception.ThemeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final SoundRepository soundRepository;

    /**
     * 전체 테마 목록을 조회합니다.
     *
     * @return List<Theme> 모든 테마 엔티티 리스트
     */
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    /**
     * 특정 테마의 상세 정보를 조회합니다.
     *
     * @param themeId 조회할 테마의 ID
     * @return ThemeDetailResponse 테마 기본 정보 + 해당 테마에 속한 사운드 리스트
     * @throws IllegalArgumentException 테마가 존재하지 않는 경우
     */
    public ThemeDetailResponse getThemeDetail(Integer themeId) {

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException(themeId));
        List<Sound> sounds = soundRepository.findByThemeId(themeId);

        return ThemeDetailResponse.from(theme, sounds);
    }

}
