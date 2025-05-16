package com.recruitment.employer_service.controller;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.employer_service.dto.request.EmployerPackageSubscriptionCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerPackageSubscriptionUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerPackageSubscriptionResponse;
import com.recruitment.employer_service.service.EmployerPackageSubscriptionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employer-package-subscription")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class EmployerPackageSubscriptionController {
    EmployerPackageSubscriptionService employerPackageSubscriptionService;

    @PostMapping
    public ApiResponse<EmployerPackageSubscriptionResponse> createSubscription(
            @RequestBody EmployerPackageSubscriptionCreationRequest request
    ) {
        return ApiResponse.<EmployerPackageSubscriptionResponse>builder()
                .result(employerPackageSubscriptionService.createSubscription(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployerPackageSubscriptionResponse> updateSubscription(
            @PathVariable UUID id,
            @RequestBody EmployerPackageSubscriptionUpdateRequest request
    ) {
        return ApiResponse.<EmployerPackageSubscriptionResponse>builder()
                .result(employerPackageSubscriptionService.updateSubscription(id, request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<EmployerPackageSubscriptionResponse>> getAllSubscriptions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ApiResponse.<PageResponse<EmployerPackageSubscriptionResponse>>builder()
                .result(employerPackageSubscriptionService.getAllSubscriptions(page,size,sortBy,sortDir ))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<EmployerPackageSubscriptionResponse> getSubscription(
            @PathVariable UUID id
    ) {
        return ApiResponse.<EmployerPackageSubscriptionResponse>builder()
                .result(employerPackageSubscriptionService.getSubscriptionById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSubscription(
            @PathVariable UUID id
    ) {
        employerPackageSubscriptionService.deleteSubscription(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }

}
