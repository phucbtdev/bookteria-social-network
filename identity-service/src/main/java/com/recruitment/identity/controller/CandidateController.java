package com.recruitment.identity.controller;

import com.recruitment.identity.dto.ApiResponse;
import com.recruitment.identity.dto.request.CandidateRegisterRequest;
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
@RequestMapping("/candidate")
public class CandidateController {

    UserService userService;

    @PostMapping("/register")
    public ApiResponse<String> createAccountCandidate(
            @Valid @RequestBody CandidateRegisterRequest request
    ) {
        userService.createAccountCandidate(request);
        return ApiResponse.<String>builder()
                .result("Candidate created successfully")
                .build();
    }
}
