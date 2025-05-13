package com.c208.sleephony;

import com.c208.sleephony.domain.theme.entity.Sound;
import com.c208.sleephony.domain.theme.entity.Theme;
import com.c208.sleephony.domain.theme.repository.SoundRepository;
import com.c208.sleephony.domain.theme.repository.ThemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // ✅ 추가
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;   // ✅ 추가
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ThemeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private SoundRepository soundRepository;

    private Theme savedTheme;

    @BeforeEach
    void setup() {
        soundRepository.deleteAll();
        themeRepository.deleteAll();

        Theme theme = Theme.builder()
                .name("Deep Forest")
                .description("숲 속의 고요한 테마")
                .imageUrl("https://example.com/image.jpg")
                .createdAt(LocalDateTime.now())
                .build();
        savedTheme = themeRepository.save(theme);

        Sound sound = Sound.builder()
                .theme(savedTheme)
                .name("Forest Rain")
                .url("https://example.com/rain.mp3")
                .sleepStage(null)
                .createdAt(LocalDateTime.now())
                .build();
        soundRepository.save(sound);
    }

    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    @DisplayName("테마 목록 조회 성공")
    void getThemes() throws Exception {
        mockMvc.perform(get("/api/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].id").value(savedTheme.getId()))
                .andExpect(jsonPath("$.results[0].name").value("Deep Forest"));
    }

    @WithMockUser(username = "testuser", roles = {"USER"})
    @Test
    @DisplayName("테마 상세 조회 성공")
    void getThemeDetail() throws Exception {
        mockMvc.perform(get("/api/themes/" + savedTheme.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.themeName").value("Deep Forest"))
                .andExpect(jsonPath("$.results.sounds[0].soundName").value("Forest Rain"));
    }
}
