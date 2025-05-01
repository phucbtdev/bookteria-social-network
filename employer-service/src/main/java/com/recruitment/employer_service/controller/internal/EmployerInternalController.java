package com.recruitment.employer_service.controller.internal;

import com.recruitment.employer_service.dto.request.EmployerCreationRequest;
import com.recruitment.employer_service.dto.response.ApiResponse;
import com.recruitment.employer_service.dto.response.EmployerResponse;
import com.recruitment.employer_service.service.EmployerService;
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
@RequestMapping("/internal/employer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerInternalController {

    EmployerService employerService;

    @PostMapping
    public ApiResponse<EmployerResponse> createEmployer(
            @RequestBody @Valid EmployerCreationRequest request
    ) {
        return ApiResponse.<EmployerResponse>builder()
                .result(employerService.createEmployer(request))
                .build();
    }
}
