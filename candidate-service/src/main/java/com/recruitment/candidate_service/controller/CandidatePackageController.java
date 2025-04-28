package com.recruitment.candidate_service.controller;

import com.recruitment.candidate_service.dto.request.CandidatePackageCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidatePackageUpdateRequest;
import com.recruitment.candidate_service.dto.response.ApiResponse;
import com.recruitment.candidate_service.dto.response.CandidatePackageResponse;
import com.recruitment.candidate_service.service.CandidatePackageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/candidate-package")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidatePackageController {
    CandidatePackageService candidatePackageService;

    @PostMapping
    ApiResponse<CandidatePackageResponse> createPackage(@RequestBody CandidatePackageCreationRequest request){
        return ApiResponse.<CandidatePackageResponse>builder()
                .result(candidatePackageService.createPackage(request))
                .build();
    }

    @PutMapping("/{packageId}")
    ApiResponse<CandidatePackageResponse> updatePackage(@PathVariable Integer packageId, @RequestBody CandidatePackageUpdateRequest request){
        return ApiResponse.<CandidatePackageResponse>builder()
                .result(candidatePackageService.updatePackage(packageId,request))
                .build();
    }

    @GetMapping
    ApiResponse<List<CandidatePackageResponse>> getAllPackages(){
        return ApiResponse.<List<CandidatePackageResponse>>builder()
                .result(candidatePackageService.getAllPackages())
                .build();
    }


    @GetMapping("/{packageId}")
    ApiResponse<CandidatePackageResponse> getPackageById(@PathVariable Integer packageId){
        return ApiResponse.<CandidatePackageResponse>builder()
                .result(candidatePackageService.getPackageById(packageId))
                .build();
    }

    @DeleteMapping("/{packageId}")
    ApiResponse<Void> deletePackage(@PathVariable Integer packageId){
        candidatePackageService.deletePackage(packageId);
        return ApiResponse.<Void>builder()
                .build();
    }

}
