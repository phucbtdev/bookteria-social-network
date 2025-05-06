package com.recruitment.candidate_service.controller;

import com.recruitment.candidate_service.dto.request.CandidateUpdateRequest;
import com.recruitment.candidate_service.dto.response.ApiResponse;
import com.recruitment.candidate_service.dto.response.CandidateResponse;
import com.recruitment.candidate_service.service.CandidateService;
import com.recruitment.common.dto.request.CandidateCreationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    ApiResponse<CandidateResponse> createCandidate(@RequestBody CandidateCreationRequest request) {
        log.info("Create candidate request: {}", request);
        return ApiResponse.<CandidateResponse>builder()
                .result(candidateService.createCandidate(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<CandidateResponse> updateCandidate(@PathVariable UUID id, @RequestBody CandidateUpdateRequest request) {
        return ApiResponse.<CandidateResponse>builder()
                .result(candidateService.updateCandidate(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<CandidateResponse>> getCandidateList() {
        return ApiResponse.<List<CandidateResponse>>builder()
                .result(candidateService.getCandidateList())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<CandidateResponse> getCandidateById( @PathVariable UUID id) {
        return ApiResponse.<CandidateResponse>builder()
                .result(candidateService.getCandidateById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteCandidate(@PathVariable UUID id) {
        candidateService.deleteCandidate(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }
}
