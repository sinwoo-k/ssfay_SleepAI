package com.c208.sleephony.domain.sleep.controller;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.dto.request.EndMeasurementRequestDto;
import com.c208.sleephony.domain.sleep.dto.response.BioDataResponseDto;
import com.c208.sleephony.domain.sleep.dto.request.StartMeasurementRequestDto;
import com.c208.sleephony.domain.sleep.service.SleepLevelService;
import com.c208.sleephony.domain.sleep.service.SleepReportService;
import com.c208.sleephony.domain.sleep.service.SleepService;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sleep/")
@AllArgsConstructor
@Tag(name = "Sleep", description = "수면 관련 CRUD")
public class SleepController {

    private final SleepService sleepService;
    private final SleepReportService sleepReportService;
    private final SleepLevelService sleepLevelService;

    @PostMapping("bio-data")
    public ResponseEntity<?> saveBioData(@RequestBody BioDataRequestDto requestDto) {
        sleepService.measureSleepStage(requestDto);
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

    @PostMapping("end-measurement")
    public ResponseEntity<?> endMeasurement(@RequestBody EndMeasurementRequestDto requestDto) {
        return ResponseEntity.ok(
                ApiResponse.success(sleepReportService.generateSleepReport(requestDto.getEndedAt()))
        );
    }

    @GetMapping("report/detail")
    public ResponseEntity<?> getReportDetail(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(sleepReportService.getReportByDate(date)));
    }

    @GetMapping("measure/from-bio")
    public ApiResponse<List<SleepPredictionResult>> measureSleepLevelsFromBioData(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<SleepPredictionResult> result = sleepLevelService.predictFromBioDataByDate(date);
        return ApiResponse.success(result);
    }
}
