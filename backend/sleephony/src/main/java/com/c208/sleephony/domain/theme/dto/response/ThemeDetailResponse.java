package com.c208.sleephony.domain.theme.dto.response;

import com.c208.sleephony.domain.sleep.entity.SleepStage;
import com.c208.sleephony.domain.theme.entity.Sound;
import com.c208.sleephony.domain.theme.entity.Theme;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThemeDetailResponse {
    private Integer themeId;
    private String themeName;
    private String themeDescription;
    private String imageUrl;
    private LocalDateTime createdAt;
    private List<SoundDto> sounds;

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

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SoundDto {
        private Integer soundId;
        private String soundName;
        private String soundUrl;
        private SleepStage sleepStage;
        private LocalDateTime createdAt;

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
