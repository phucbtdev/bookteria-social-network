package com.recruitment.job_service.controller;

import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.job_service.dto.request.JobCreationRequest;
import com.recruitment.job_service.dto.request.JobFilterRequest;
import com.recruitment.job_service.dto.request.JobUpdateRequest;
import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/job")
public class JobController {

    JobService jobService;

    @PostMapping
    ApiResponse<JobResponse> postJob(@Valid  @RequestBody JobCreationRequest request ) {
        return ApiResponse.<JobResponse>builder()
                .result(jobService.createJob(request))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID")
    public ApiResponse<JobResponse> getJobById(
            @Parameter(description = "Job ID") @PathVariable UUID id) {
        log.info("REST request to get job by ID: {}", id);
        JobResponse response = jobService.getJobById(id);
        return ApiResponse.<JobResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get job by slug")
    public ApiResponse<JobResponse> getJobBySlug(
            @Parameter(description = "Job slug") @PathVariable String slug) {
        log.info("REST request to get job by slug: {}", slug);
        JobResponse response = jobService.getJobBySlug(slug);
        return ApiResponse.<JobResponse>builder()
                .result(response)
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update job")
    public ApiResponse<JobResponse> updateJob(
            @Parameter(description = "Job ID") @PathVariable UUID id,
            @Valid @RequestBody JobUpdateRequest request) {
        log.info("REST request to update job: {}", id);
        JobResponse response = jobService.updateJob(id, request);
        return ApiResponse.<JobResponse>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job")
    public ApiResponse<Void> deleteJob(
            @Parameter(description = "Job ID") @PathVariable UUID id) {
        log.info("REST request to delete job: {}", id);
        jobService.deleteJob(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .build();
    }

    @GetMapping
    ApiResponse<PageResponse<JobResponse>> getJobList(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(jobService.getAllJobs(pageNo,pageSize,sortBy,sortDir))
                .build();
    }

    @GetMapping("/employer/{employerId}")
    @Operation(summary = "Get jobs by employer")
    public ApiResponse<PageResponse<JobResponse>> getJobsByEmployer(
            @Parameter(description = "Employer ID") @PathVariable UUID employerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        log.info("REST request to get jobs by employer: {}", employerId);
        PageResponse<JobResponse> response = jobService.getJobsByEmployer(employerId, page, size, sortBy, sortDirection);
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(response)
                .build();
    }

    @PostMapping("/search")
    @Operation(summary = "Search jobs with filters")
    public ApiResponse<PageResponse<JobResponse>> searchJobs(@RequestBody JobFilterRequest filterRequest) {
        log.info("REST request to search jobs with filters");
        PageResponse<JobResponse> response = jobService.searchJobs(filterRequest);
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(response)
                .build();
    }

    @PatchMapping("/{id}/approve")
    @Operation(summary = "Approve job posting")
    public ApiResponse<JobResponse> approveJob(
            @Parameter(description = "Job ID") @PathVariable UUID id) {
        log.info("REST request to approve job: {}", id);
        JobResponse response = jobService.approveJob(id);
        return ApiResponse.<JobResponse>builder()
                .result(response)
                .build();
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Reject job posting")
    public ApiResponse<JobResponse> rejectJob(
            @Parameter(description = "Job ID") @PathVariable UUID id) {
        log.info("REST request to reject job: {}", id);
        JobResponse response = jobService.rejectJob(id);
        return ApiResponse.<JobResponse>builder()
                .result(response)
                .build();
    }
}
