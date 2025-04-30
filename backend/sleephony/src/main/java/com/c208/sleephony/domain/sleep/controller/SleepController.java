package com.c208.sleephony.domain.sleep.controller;

import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.dto.response.BioDataResponseDto;
import com.c208.sleephony.domain.sleep.dto.request.StartMeasurementRequestDto;
import com.c208.sleephony.domain.sleep.service.SleepService;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sleep")
@AllArgsConstructor
@Tag(name = "Sleep", description = "수면 관련 CRUD")
public class SleepController {

    private final SleepService sleepService;

    @PostMapping("bio-data")
    public ResponseEntity<?> saveDioData(@RequestBody BioDataRequestDto requestDto) {
        sleepService.saveAll(requestDto);
        return ResponseEntity.ok(ApiResponse.success(
                new BioDataResponseDto(200, "SU", "OK", "NREM1")
        ));
    }

    @PostMapping("start-measurement")
    public ResponseEntity<?> startMeasurement(@RequestBody StartMeasurementRequestDto requestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(sleepService.startMeasurement(requestDto.getStartedAt()))
        );
    }
}
