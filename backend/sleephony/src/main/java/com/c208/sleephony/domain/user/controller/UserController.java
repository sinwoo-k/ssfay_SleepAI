package com.c208.sleephony.domain.user.controller;

import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.service.UserService;
import com.c208.sleephony.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
