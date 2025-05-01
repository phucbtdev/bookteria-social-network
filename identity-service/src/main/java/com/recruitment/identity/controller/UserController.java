package com.recruitment.identity.controller;

import java.util.List;

import com.recruitment.identity.dto.request.EmployerRegisterRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.recruitment.identity.dto.ApiResponse;
import com.recruitment.identity.dto.request.UserUpdateRequest;
import com.recruitment.identity.dto.response.UserResponse;
import com.recruitment.identity.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/register")
    ApiResponse<UserResponse> createUser(
            @RequestBody @Valid EmployerRegisterRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createAccountEmployer(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(
            @PathVariable("userId") String userId
    ) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(
            @PathVariable String userId
    ) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("Users has been deleted")
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(
            @PathVariable String userId,
            @RequestBody UserUpdateRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
}
