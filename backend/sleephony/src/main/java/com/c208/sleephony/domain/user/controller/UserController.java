package com.c208.sleephony.domain.user.controller;

import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.dto.response.UserSleepLevel;
import com.c208.sleephony.domain.user.dto.response.UserSummaryDto;
import com.c208.sleephony.domain.user.service.UserService;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "User", description = "유저 관련 CRUD")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 프로필 등록")
    @PutMapping("/profile")
    public ApiResponse<String> createUserProfile(@Valid @RequestBody CreateUserProfileRequest request) {
        userService.createUserProfile(request);
        return ApiResponse.success(HttpStatus.OK, "프로필이 등록되었습니다.");
    }

    @Operation(summary = "유저 프로필 수정")
    @PatchMapping("/profile")
    public ApiResponse<String> updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        userService.updateUserProfile(request);
        return ApiResponse.success(HttpStatus.OK, "프로필이 수정되었습니다.");
    }

    @Operation(summary = "유저 프로필 조회")
    @GetMapping("/profile")
    public ApiResponse<GetUserProfileResponse> getUserProfile() {
        return ApiResponse.success(HttpStatus.OK, userService.getUserProfileResponse());
    }

    @Operation(summary = "유저 삭제")
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUserProfile() {
        userService.deleteUser();
        return ApiResponse.success(HttpStatus.OK, "회원 탈퇴가 완료되었습니다.");
    }
    @Operation(summary = "전체 회원 요약 조회")
    @GetMapping("/all")
    public ApiResponse<List<UserSummaryDto>> getAllUsers() {
        return ApiResponse.success(HttpStatus.OK, userService.getAllUsers());
    }

    @Operation(summary = "특정 회원 측정중 여부")
    @GetMapping("/status/{userId}")
    public ApiResponse<Boolean> isMeasuring(@PathVariable Integer userId) {
        return ApiResponse.success(HttpStatus.OK, userService.isMeasuring(userId));
    }

    @Operation(summary = "특정 회원의 현재 세션 수면 단계 목록")
    @GetMapping("/sleep-levels/{userId}")
    public ApiResponse<List<UserSleepLevel>> getSleepLevels(@PathVariable Integer userId) {
        return ApiResponse.success(HttpStatus.OK, userService.getRecentSleepLevels(userId));
    }
}
