package com.c208.sleephony.domain.user.controller;

import com.c208.sleephony.domain.user.dto.request.CreateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.request.UpdateUserProfileRequest;
import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/`user")
@AllArgsConstructor
@Tag(name = "User", description = "유저 관련 CRUD")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 프로필 등록")
    @PutMapping("/profile/{userId}")
    public ResponseEntity<Void> createUserProfile(@PathVariable Integer userId,
                                                  @RequestBody CreateUserProfileRequest request) {
        userService.createUserProfile(userId, request);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "유저 프로필 수정")
    @PatchMapping("/profile/{userId}")
    public ResponseEntity<Void> updateUserProfile(@PathVariable Integer userId,
                                                  @RequestBody UpdateUserProfileRequest request) {
        userService.updateUserProfile(userId, request);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "유저 프로필 조회")
    @GetMapping("/profile/{userId}")
    public ResponseEntity<GetUserProfileResponse> getUserProfile(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUserProfileResponse(userId));
    }

}
