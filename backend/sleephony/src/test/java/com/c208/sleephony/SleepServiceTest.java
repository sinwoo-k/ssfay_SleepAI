package com.c208.sleephony;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.service.SleepService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
public class SleepServiceTest {

    private final SleepService sleepService;

    @Test
    void testSleep() {
        BioDataRequestDto.DataPoint point = BioDataRequestDto.DataPoint.builder()
                .time(LocalDateTime.now().toString())
                .heartRate(55)
                .gyroX(0.1f)
                .gyroY(0.1f)
                .gyroZ(0.1f)
                .build();

        BioDataRequestDto dto = BioDataRequestDto.builder()
                .measuredAt(LocalDateTime.now().toString())
                .data(List.of(point))
                .build();

        List<SleepPredictionResult> result = sleepService.measureSleepStage(dto);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getLevel()).isNotBlank();
    }
}
