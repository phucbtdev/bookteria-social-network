package com.recruitment.candidate_service.controller;

import com.recruitment.candidate_service.dto.request.CandidatePackageSubscriptionCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidatePackageSubscriptionUpdateRequest;
import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.candidate_service.dto.response.CandidatePackageSubscriptionResponse;
import com.recruitment.candidate_service.service.CandidatePackageSubscriptionService;
import com.recruitment.common.dto.response.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/candidate-package-subscription")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CandidatePackageSubscriptionController {

    CandidatePackageSubscriptionService candidatePackageSubscriptionService;

    @PostMapping
    public ApiResponse<CandidatePackageSubscriptionResponse> createCandidatePackageSubscription(
            @RequestBody CandidatePackageSubscriptionCreationRequest request
    ){
            return ApiResponse.<CandidatePackageSubscriptionResponse>builder()
                    .result(candidatePackageSubscriptionService.createCandidatePackageSubscription(request))
                    .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CandidatePackageSubscriptionResponse> updateCandidatePackageSubscription(
            @PathVariable UUID id,
            @RequestBody CandidatePackageSubscriptionUpdateRequest request
    ){
        return ApiResponse.<CandidatePackageSubscriptionResponse>builder()
                .result(candidatePackageSubscriptionService.updateCandidatePackageSubscription(id, request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<CandidatePackageSubscriptionResponse>> getAllCandidatePackageSubscriptions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir

    ){
        return ApiResponse.<PageResponse<CandidatePackageSubscriptionResponse>>builder()
                .result(candidatePackageSubscriptionService.getAllCandidatePackageSubscriptions(page,size,sortBy,sortDir))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CandidatePackageSubscriptionResponse> getCandidatePackageSubscription(
            @PathVariable UUID id
    ){
        return ApiResponse.<CandidatePackageSubscriptionResponse>builder()
                .result(candidatePackageSubscriptionService.getCandidatePackageSubscription(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCandidatePackageSubscription(
            @PathVariable UUID id
    ){
        candidatePackageSubscriptionService.deleteCandidatePackageSubscription(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }

}
