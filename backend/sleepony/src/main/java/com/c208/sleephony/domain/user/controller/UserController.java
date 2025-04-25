package com.c208.sleephony.domain.user.controller;

import com.c208.sleephony.domain.user.dto.response.GetUserProfileResponse;
import com.c208.sleephony.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "User", description = "유저 관련 CRUD")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 프로필 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<GetUserProfileResponse> getUserProfile(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUserProfileResponse(userId));
    }

}
