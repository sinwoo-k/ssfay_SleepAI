package com.c208.sleephony.domain.preview.controller;

import com.c208.sleephony.domain.preview.service.PreviewService;
import com.c208.sleephony.domain.preview.dto.response.UserSleepLevel;
import com.c208.sleephony.domain.preview.dto.response.UserSummaryDto;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/preview/")
@RequiredArgsConstructor
@Tag(name = "Preview", description = "시연관련 API")
public class PreviewController {
    private final PreviewService previewService;

    @Operation(summary = "전체 회원 요약 조회")
    @GetMapping("/all")
    public ApiResponse<List<UserSummaryDto>> getAllUsers() {
        return ApiResponse.success(HttpStatus.OK, previewService.getAllUsers());
    }

    @Operation(summary = "특정 회원 측정중 여부")
    @GetMapping("/status/{userId}")
    public ApiResponse<Boolean> isMeasuring(@PathVariable Integer userId) {
        return ApiResponse.success(HttpStatus.OK, previewService.isMeasuring(userId));
    }

    @Operation(summary = "특정 회원의 현재 세션 수면 단계 목록")
    @GetMapping("/sleep-levels/{userId}")
    public ApiResponse<List<UserSleepLevel>> getSleepLevels(@PathVariable Integer userId) {
        return ApiResponse.success(HttpStatus.OK, previewService.getRecentSleepLevels(userId));
    }
}
