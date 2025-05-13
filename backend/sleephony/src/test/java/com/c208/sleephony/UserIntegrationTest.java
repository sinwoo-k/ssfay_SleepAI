package com.c208.sleephony;


import com.c208.sleephony.domain.sleep.repository.BioRepository;
import com.c208.sleephony.domain.sleep.repository.SleepLevelRepository;
import com.c208.sleephony.domain.sleep.repository.SleepReportRepository;
import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
import com.c208.sleephony.domain.user.entity.Social;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BioRepository bioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SleepLevelRepository sleepLevelRepository;

    @Autowired
    private SleepReportRepository sleepReportRepository;

    private User savedUser;

    @BeforeEach
    void setup() {
        sleepReportRepository.deleteAllInBatch();
        bioRepository.deleteAllInBatch();
        sleepLevelRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        savedUser = User.builder()
                .email("test@example.com")
                .social(Social.GOOGLE)
                .build();
        savedUser = userRepository.save(savedUser);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                savedUser.getUserId(), null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("유저 프로필 등록 성공")
    void createUserProfile() throws Exception {
        CreateUserProfileRequest request = CreateUserProfileRequest.builder()
                .nickname("소연")
                .height(165f)
                .weight(50f)
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender("F")
                .build();

        mockMvc.perform(put("/api/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").value("프로필이 등록되었습니다."));
    }

    @Test
    @DisplayName("유저 프로필 조회 성공")
    void getUserProfile() throws Exception {
        // 먼저 등록부터 진행
        savedUser.setNickname("소연");
        savedUser.setHeight(165f);
        savedUser.setWeight(50f);
        savedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        savedUser.setGender("F");
        userRepository.save(savedUser);

        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.nickname").value("소연"));
    }

    @Test
    @DisplayName("유저 프로필 수정 성공")
    void updateUserProfile() throws Exception {
        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
                .nickname("변경된이름")
                .height(170f)
                .weight(60f)
                .birthDate(LocalDate.of(1995, 5, 5))
                .gender("M")
                .build();

        mockMvc.perform(patch("/api/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").value("프로필이 수정되었습니다."));
    }

    @Test
    @DisplayName("유저 삭제 성공")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results").value("회원 탈퇴가 완료되었습니다."));
    }
}
