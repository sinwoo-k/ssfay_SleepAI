package com.c208.sleephony.domain.user.controller;

import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.service.UserService;
import com.c208.sleephony.global.response.ApiResponse;
import com.c208.sleephony.global.response.MessageOnlyResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "User", description = "유저 관련 CRUD")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 프로필 등록")
    @PutMapping("/profile")
    public ApiResponse<MessageOnlyResponse> createUserProfile(@Valid @RequestBody CreateUserProfileRequest request) {
        userService.createUserProfile(request);
        return ApiResponse.success(HttpStatus.OK, new MessageOnlyResponse("프로필이 등록되었습니다."));
    }


    @Operation(summary = "유저 프로필 수정")
    @PatchMapping("/profile")
    public ApiResponse<MessageOnlyResponse> updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        userService.updateUserProfile(request);
        return ApiResponse.success(HttpStatus.OK, new MessageOnlyResponse("프로필이 수정되었습니다."));
    }


    @Operation(summary = "유저 프로필 조회")
    @GetMapping("/profile")
    public ApiResponse<GetUserProfileResponse> getUserProfile() {
        return ApiResponse.success(HttpStatus.OK, userService.getUserProfileResponse());
    }

    @Operation(summary = "유저 삭제")
    @DeleteMapping("/delete")
    public ApiResponse<MessageOnlyResponse> deleteUserProfile() {
        userService.deleteUser();
        return ApiResponse.success(HttpStatus.OK, new MessageOnlyResponse("회원 탈퇴가 완료되었습니다."));
    }

}
