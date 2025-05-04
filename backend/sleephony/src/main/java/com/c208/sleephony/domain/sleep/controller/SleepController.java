package com.c208.sleephony.domain.sleep.controller;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequestDto;
import com.c208.sleephony.domain.sleep.dto.request.EndMeasurementRequestDto;
import com.c208.sleephony.domain.sleep.dto.request.StartMeasurementRequestDto;
import com.c208.sleephony.domain.sleep.dto.response.SleepGraphPointDto;
import com.c208.sleephony.domain.sleep.entity.SleepReport;
import com.c208.sleephony.domain.sleep.service.SleepService;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "생체 데이터 저장 및 수면 단계 예측", description = "사용자가 전송한 생체 데이터를 저장하고 수면 단계 및 점수를 예측합니다.")
    @PostMapping("bio-data")
    public ApiResponse<List<SleepPredictionResult>> saveBioData(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "생체 데이터 요청 DTO")
            @RequestBody BioDataRequestDto requestDto
    ) {
        return ApiResponse.success(HttpStatus.CREATED, sleepService.measureSleepStage(requestDto));
    }

    @Operation(summary = "수면 측정 시작 시간 저장", description = "Redis에 수면 측정 시작 시간을 저장합니다.")
    @PostMapping("start-measurement")
    public ApiResponse<String> startMeasurement(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "측정 시작 시간 DTO")
            @RequestBody StartMeasurementRequestDto requestDto
    ) {
        return ApiResponse.success(HttpStatus.CREATED,sleepService.startMeasurement(requestDto.getStartedAt()));
    }

    @Operation(summary = "수면 리포트 생성", description = "수면 측정 종료 시간을 기준으로 수면 리포트를 생성합니다.")
    @PostMapping("end-measurement")
    public ApiResponse<SleepReport> endMeasurement(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "측정 종료 시간 DTO")
            @RequestBody EndMeasurementRequestDto requestDto
    ) {
        return ApiResponse.success(HttpStatus.CREATED,sleepService.generateSleepReport(requestDto.getEndedAt()));
    }

    @Operation(summary = "수면 리포트 상세 조회", description = "특정 날짜의 수면 리포트를 조회합니다.")
    @GetMapping("report/detail/{date}")
    public ApiResponse<SleepReport> getReportDetail(
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(HttpStatus.OK,sleepService.getReportByDate(date));
    }

    @Operation(summary = "수면 단계 그래프 데이터 조회", description = "특정 날짜에 해당하는 수면 단계/점수 데이터를 반환하여 그래프를 그릴 수 있도록 합니다.")
    @GetMapping("report/graph/{date}")
    public ApiResponse<List<SleepGraphPointDto>> getSleepGraph(
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(HttpStatus.OK,sleepService.getSleepGraphPoints(date));
    }

    @Operation(summary = "생체 데이터 기반 수면 단계 재예측", description = "저장된 생체 데이터를 바탕으로 수면 단계 및 점수를 다시 예측하고 저장합니다.")
    @GetMapping("measure/from-bio")
    public ApiResponse<List<SleepPredictionResult>> measureSleepLevelsFromBioData(
            @Parameter(description = "측정 대상 날짜 (yyyy-MM-dd)", required = true)
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(HttpStatus.OK,sleepService.predictFromBioDataByDate(date));
    }
}
