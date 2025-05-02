package com.c208.sleephony.domain.sleep.controller;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.dto.request.EndMeasurementRequestDto;
import com.c208.sleephony.domain.sleep.dto.request.StartMeasurementRequestDto;
import com.c208.sleephony.domain.sleep.dto.response.SleepGraphPointDto;
import com.c208.sleephony.domain.sleep.entity.SleepReport;
import com.c208.sleephony.domain.sleep.service.SleepLevelService;
import com.c208.sleephony.domain.sleep.service.SleepReportService;
import com.c208.sleephony.domain.sleep.service.SleepService;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public ApiResponse<List<SleepPredictionResult>> saveBioData(@RequestBody BioDataRequestDto requestDto) {
        return ApiResponse.success(HttpStatus.CREATED, sleepService.measureSleepStage(requestDto));
    }

    @PostMapping("start-measurement")
    public ApiResponse<String> startMeasurement(@RequestBody StartMeasurementRequestDto requestDto) {
        return ApiResponse.success(HttpStatus.CREATED, sleepService.startMeasurement(requestDto.getStartedAt()));
    }

    @PostMapping("end-measurement")
    public ApiResponse<SleepReport> endMeasurement(@RequestBody EndMeasurementRequestDto requestDto) {
        return ApiResponse.success(HttpStatus.CREATED,sleepReportService.generateSleepReport(requestDto.getEndedAt()));
    }

    @GetMapping("report/detail/{date}")
    public ApiResponse<SleepReport> getReportDetail(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(HttpStatus.OK,sleepReportService.getReportByDate(date));
    }

    @GetMapping("report/graph/{date}")
    public ApiResponse<List<SleepGraphPointDto>> getSleepGraph(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(HttpStatus.OK,sleepReportService.getSleepGraphPoints(date));
    }

    @GetMapping("measure/from-bio")
    public ApiResponse<List<SleepPredictionResult>> measureSleepLevelsFromBioData(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(HttpStatus.OK,sleepLevelService.predictFromBioDataByDate(date));
    }
}
