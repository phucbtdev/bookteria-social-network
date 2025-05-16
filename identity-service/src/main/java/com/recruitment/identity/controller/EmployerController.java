package com.recruitment.identity.controller;

import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.identity.dto.request.EmployerRegisterRequest;
import com.recruitment.identity.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/employer")
public class EmployerController {

    UserService userService;

    @PostMapping("/register")
    ApiResponse<String> createUser(
            @Valid @RequestBody EmployerRegisterRequest request
    ) {
        userService.createAccountEmployer(request);
        return ApiResponse.<String>builder()
                .result("Employer created successfully")
                .build();
    }
}
