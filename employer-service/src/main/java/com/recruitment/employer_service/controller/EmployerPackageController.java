package com.recruitment.employer_service.controller;

import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.employer_service.dto.request.EmployerPackageCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerPackageUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerPackageResponse;
import com.recruitment.employer_service.service.EmployerPackageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employer-package")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerPackageController {

    EmployerPackageService employerPackageService;

    @PostMapping
    public ApiResponse<EmployerPackageResponse> createEmployerPackage( @RequestBody EmployerPackageCreationRequest request) {
        return ApiResponse.<EmployerPackageResponse>builder()
                .result(employerPackageService.createEmployerPackage(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployerPackageResponse> updateEmployerPackage( @PathVariable Integer id , @RequestBody EmployerPackageUpdateRequest request) {
        return ApiResponse.<EmployerPackageResponse>builder()
                .result(employerPackageService.updateEmployerPackage(id,request))
                .build();
    }

    @GetMapping
    public ApiResponse<PageResponse<EmployerPackageResponse>> getAllEmployerPackages(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir

    ) {
        return ApiResponse.<PageResponse<EmployerPackageResponse>>builder()
                .result(employerPackageService.getAllEmployerPackages(page,size,sortBy,sortDir))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<EmployerPackageResponse> getEmployerPackage(@PathVariable Integer id) {
        return ApiResponse.<EmployerPackageResponse>builder()
                .result(employerPackageService.getEmployerPackageById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEmployerPackage( @PathVariable Integer id) {
        employerPackageService.deleteEmployerPackage(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }

}
