package com.c208.sleephony.domain.sleep.controller;

import com.c208.sleephony.domain.sleep.dto.SleepPredictionResult;
import com.c208.sleephony.domain.sleep.dto.request.BioDataRequest;
import com.c208.sleephony.domain.sleep.dto.request.EndMeasurementRequest;
import com.c208.sleephony.domain.sleep.dto.request.StartMeasurementRequest;
import com.c208.sleephony.domain.sleep.dto.request.StatisticsRequest;
import com.c208.sleephony.domain.sleep.dto.response.CombinedStatResponse;
import com.c208.sleephony.domain.sleep.dto.response.GraphResponse;
import com.c208.sleephony.domain.sleep.dto.response.SleepGraphPoint;
import com.c208.sleephony.domain.sleep.dto.response.SleepReportWithPrevious;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sleep/")
@AllArgsConstructor
@Tag(name = "Sleep", description = "수면 관련 CRUD")
public class SleepController {

    private final SleepService sleepService;

    @Operation(summary = "생체 데이터 저장 및 수면 단계 예측", description = "사용자가 전송한 생체 데이터를 저장하고 수면 단계 및 점수를 예측합니다.")
    @PostMapping("bio-data")
    public ApiResponse<SleepPredictionResult> saveBioData(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "생체 데이터 요청 DTO")
            @RequestBody BioDataRequest requestDto
    ) {
        return ApiResponse.success(HttpStatus.CREATED, sleepService.analyzeSleepStageDirectly(requestDto));
    }

    @Operation(summary = "수면 측정 시작 시간 저장", description = "Redis에 수면 측정 시작 시간을 저장합니다.")
    @PostMapping("start-measurement")
    public ApiResponse<String> startMeasurement(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "측정 시작 시간 DTO")
            @RequestBody StartMeasurementRequest requestDto
    ) {
        return ApiResponse.success(HttpStatus.CREATED,sleepService.startMeasurement(requestDto.getStartedAt()));
    }

    @Operation(summary = "수면 리포트 생성", description = "수면 측정 종료 시간을 기준으로 수면 리포트를 생성합니다.")
    @PostMapping("end-measurement")
    public ApiResponse<SleepReport> endMeasurement(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "측정 종료 시간 DTO")
            @RequestBody EndMeasurementRequest requestDto
    ) {
        return ApiResponse.success(HttpStatus.CREATED,sleepService.generateSleepReport(requestDto.getEndedAt()));
    }

    @Operation(summary = "수면 리포트 상세 조회", description = "특정 날짜의 수면 리포트를 조회합니다.")
    @GetMapping("report/detail/{date}")
    public ApiResponse<SleepReportWithPrevious> getReportDetail(
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(HttpStatus.OK,sleepService.getReportByDate(date));
    }

    @Operation(summary = "수면 단계 그래프 데이터 조회", description = "특정 날짜에 해당하는 수면 단계/점수 데이터를 반환하여 그래프를 그릴 수 있도록 합니다.")
    @GetMapping("report/graph/{date}")
    public ApiResponse<List<SleepGraphPoint>> getSleepGraph(
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(HttpStatus.OK,sleepService.getSleepGraphPoints(date));
    }

    @Operation(
            summary = "생체 데이터 기반 수면 단계 재예측",
            description = "저장된 생체 데이터를 바탕으로 주어진 기간의 수면 단계 및 점수를 다시 예측하고 저장합니다."
    )
    @GetMapping("/measure/from-bio")
    public ApiResponse<List<SleepPredictionResult>> measureSleepLevelsFromBioData(
            @Parameter(
                    description = "예측 시작 일시 (ISO-8601, yyyy-MM-dd'T'HH:mm:ss)",
                    required = true
            )
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDateTime,

            @Parameter(
                    description = "예측 종료 일시 (ISO-8601, yyyy-MM-dd'T'HH:mm:ss)",
                    required = true
            )
            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDateTime
    ) {
        List<SleepPredictionResult> results =
                sleepService.predictFromBioDataByRange(startDateTime, endDateTime);
        return ApiResponse.success(HttpStatus.OK, results);
    }

    @Operation(summary = "GPT API를 통한 AI 리포트", description = "기록된 리포트를 바탕으로 GPT에게 프롬프트를 이용한 AI 분석 리포트 제공")
    @GetMapping("ai-report/{date}")
    public ApiResponse<String> measureSleepLevelsFromAIData(
            @Parameter(description = "측정 대상 날짜 (yyyy-MM-dd)", required = true)
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success(HttpStatus.OK, sleepService.advise(sleepService.getReportByDate(date)));
    }

    @Operation(summary = "통계 요약 API", description = "주, 월, 년 별로 데이터 통계 요약 API")
    @PostMapping("/stat/summary")
    public ApiResponse<CombinedStatResponse> getSummary(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "통계 요청 DTO")
            @RequestBody StatisticsRequest req
    ) {
        return ApiResponse.success(HttpStatus.OK, sleepService.getCombinedStats(req));
    }

    @Operation(
            summary = "수면 통계 그래프 데이터",
            description = "지정된 기간 및 단위에 따라 그래프를 그릴 수 있는 수면 통계 데이터를 반환합니다."
    )
    @PostMapping("/stat/graph")
    public ApiResponse<GraphResponse> getGraph(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "통계 요청 DTO")
            @RequestBody StatisticsRequest req
    ) {
        GraphResponse graph = sleepService.graph(req);
        return ApiResponse.success(HttpStatus.OK, graph);
    }

    @Operation(
            summary = "월별 수면 리포트 날짜 조회",
            description = "로그인된 사용자의 지정된 연월(month)에 수면 리포트가 존재하는 날짜 목록을 반환합니다."
    )
    @GetMapping("/reports/dates")
    public ApiResponse<List<LocalDate>> getReportDates(
            @RequestParam String month
    ){
        return ApiResponse.success(HttpStatus.OK,sleepService.getReportedDatesForMonth(month));
    }

}
