package com.c208.sleephony.domain.theme.dto.response;

import com.c208.sleephony.domain.theme.entity.Sound;
import com.c208.sleephony.domain.theme.entity.Theme;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ThemeDetailResponse(
        Integer themeId,
        String themeName,
        String themeDescription,
        String imageUrl,
        LocalDateTime createdAt,
        List<SoundDto> sounds
) {

    public static ThemeDetailResponse from(Theme theme, List<Sound> sounds) {
        return ThemeDetailResponse.builder()
                .themeId(theme.getId())
                .themeName(theme.getName())
                .themeDescription(theme.getDescription())
                .imageUrl(theme.getImageUrl())
                .createdAt(theme.getCreatedAt())
                .sounds(sounds.stream().map(SoundDto::from).toList())
                .build();
    }

    public record SoundDto(
            Integer soundId,
            String soundName,
            String soundUrl,
            String sleepStage,
            LocalDateTime createdAt
    ) {
        public static SoundDto from(Sound sound) {
            return new SoundDto(
                    sound.getId(),
                    sound.getName(),
                    sound.getUrl(),
                    sound.getSleepStage(),
                    sound.getCreatedAt()
            );
        }
    }
}
