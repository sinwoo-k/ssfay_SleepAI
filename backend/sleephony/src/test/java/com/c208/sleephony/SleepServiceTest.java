package com.c208.sleephony;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequest;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequest;
import com.c208.sleephony.domain.sleep.entity.SleepStatistics;
import com.c208.sleephony.domain.sleep.service.SleepService;
import com.c208.sleephony.domain.user.entity.User;
import com.c208.sleephony.domain.user.repsotiry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class SleepServiceTest {

    @Autowired
    private SleepService sleepService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setupAuthentication() {
        User savedUser = userRepository.save(User.builder()
                .email("test@sleephony.com")
                .birthDate(LocalDate.of(1990, 1, 1))
                .nickname("테스트유저")
                .gender("F")
                .height(165.0f)
                .weight(55.0f)
                .build());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(savedUser.getUserId().toString(), null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testSleep() {
        BioDataRequest.DataPoint point = BioDataRequest.DataPoint.builder()
                .time(LocalDateTime.now().toString())
                .heartRate(55)
                .temperature(35.5f)
                .gyroX(0.1f)
                .gyroY(0.1f)
                .gyroZ(0.1f)
                .build();

        BioDataRequest dto = BioDataRequest.builder()
                .measuredAt(LocalDateTime.now().toString())
                .data(List.of(point))
                .build();

        SleepPredictionResult result = sleepService.measureSleepStage(dto);

        assertThat(result).isNotNull();
        assertThat(result.getLevel()).isNotBlank();
    }
}
