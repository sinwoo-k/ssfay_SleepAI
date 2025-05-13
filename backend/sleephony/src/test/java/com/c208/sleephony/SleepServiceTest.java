package com.c208.sleephony;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequest;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequest;
import com.c208.sleephony.domain.sleep.service.SleepService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
public class SleepServiceTest {

    @Autowired
    private final SleepService sleepService;

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
