package com.devteria.profile.controller;

import com.devteria.profile.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserRepositoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserRepositoryService userRepositoryService;

    @GetMapping("/users/{userId}")
    UserProfileResponse getUserProfile(@PathVariable("userId") String id) {
        return userRepositoryService.getProfile(id);
    }

    @GetMapping("/users")
    ApiResponse<List<UserProfileResponse>> getAllProfiles() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userRepositoryService.getAllProfile())
                .build();
    }
}
