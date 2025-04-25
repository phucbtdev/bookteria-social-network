package com.devteria.profile.controller;

import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserRepositoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {

    UserRepositoryService userRepositoryService;

    @PostMapping("/internal/users")
    UserProfileResponse createUserProfile(@RequestBody ProfileCreationRequest request) {
        return userRepositoryService.createProfile(request);
    }

    @GetMapping("/internal/users/{userId}")
    UserProfileResponse getUserProfile(@PathVariable String userId) {
        var data = userRepositoryService.getProfileByUserId(userId);
        log.info("Fetching user profile for data: {}", data);
        return data;
    }
}
