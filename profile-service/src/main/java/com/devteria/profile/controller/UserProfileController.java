package com.devteria.profile.controller;

import com.devteria.profile.dto.request.UserProfileCreation;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserRepositoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserRepositoryService userRepositoryService;

    @PostMapping("/users")
    UserProfileResponse createUserProfile(@RequestBody UserProfileCreation request) {
        return userRepositoryService.createProfile(request);
    }

    @GetMapping("/users/{userId}")
    UserProfileResponse getUserProfile(@PathVariable("userId") String id) {
        return userRepositoryService.getProfile(id);
    }
}
