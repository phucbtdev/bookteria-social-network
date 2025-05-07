package com.recruitment.candidate_service.controller;

import com.recruitment.candidate_service.dto.request.CandidateUpdateRequest;
import com.recruitment.candidate_service.dto.response.ApiResponse;
import com.recruitment.candidate_service.dto.response.CandidateResponse;
import com.recruitment.candidate_service.service.CandidateService;
import com.recruitment.common.dto.request.CandidateCreationRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/candidate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidateController {

    CandidateService candidateService;

    @PostMapping
    ApiResponse<CandidateResponse> createCandidate(
            @Valid @RequestBody CandidateCreationRequest request
    ) {
        log.info("Create candidate request: {}", request);
        return ApiResponse.<CandidateResponse>builder()
                .result(candidateService.createCandidate(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}")
    ApiResponse<CandidateResponse> updateCandidate(
            @PathVariable UUID userId, @Valid @RequestBody CandidateUpdateRequest request
    ) {
        return ApiResponse.<CandidateResponse>builder()
                .result(candidateService.updateCandidate(userId, request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ApiResponse<List<CandidateResponse>> getCandidateList() {
        return ApiResponse.<List<CandidateResponse>>builder()
                .result(candidateService.getCandidateList())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    ApiResponse<CandidateResponse> getCandidateById(
            @PathVariable UUID id
    ) {
        return ApiResponse.<CandidateResponse>builder()
                .result(candidateService.getCandidateById(id))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Void> deleteCandidate(
            @PathVariable String userId
    ) {
        candidateService.deleteCandidate(userId);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }
}
