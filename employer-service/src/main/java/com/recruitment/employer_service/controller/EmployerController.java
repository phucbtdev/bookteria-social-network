package com.recruitment.employer_service.controller;

import com.recruitment.common.dto.request.EmployerCreationRequest;
import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.employer_service.dto.request.EmployerUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerResponse;
import com.recruitment.employer_service.service.EmployerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerController {
    EmployerService employerService;

    @PostMapping
    public ApiResponse<EmployerResponse> createEmployer(@RequestBody EmployerCreationRequest request) {
        return ApiResponse.<EmployerResponse>builder()
                .result(employerService.createEmployer(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployerResponse> updateEmployer(@PathVariable UUID id, @RequestBody EmployerUpdateRequest request) {
        return ApiResponse.<EmployerResponse>builder()
                .result(employerService.updateEmployer(id, request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<EmployerResponse>> getAllEmployers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir

    ) {
        return ApiResponse.<PageResponse<EmployerResponse>>builder()
                .result(employerService.getAllEmployers(page,size,sortBy,sortDir))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<EmployerResponse> getEmployerById(@PathVariable UUID id) {
        return ApiResponse.<EmployerResponse>builder()
                .result(employerService.getEmployerById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEmployer(@PathVariable UUID id) {
        employerService.deleteEmployer(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }
}
