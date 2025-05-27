package com.recruitment.job_service.controller;

import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.job_service.dto.request.JobApplicationCreateRequest;
import com.recruitment.job_service.dto.request.JobApplicationUpdateRequest;
import com.recruitment.job_service.dto.response.JobApplicationResponse;
import com.recruitment.job_service.entity.JobApplication;
import com.recruitment.job_service.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobApplicationResponse>> create(@Valid @RequestBody JobApplicationCreateRequest request) {
        JobApplicationResponse response = jobApplicationService.create(request);
        ApiResponse<JobApplicationResponse> apiResponse = ApiResponse.<JobApplicationResponse>builder()
                .message("Job application created successfully")
                .result(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> getById(@PathVariable UUID id) {
        JobApplicationResponse response = jobApplicationService.getById(id);
        ApiResponse<JobApplicationResponse> apiResponse = ApiResponse.<JobApplicationResponse>builder()
                .message("Job application retrieved successfully")
                .result(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody JobApplicationUpdateRequest request) {

        JobApplicationResponse response = jobApplicationService.update(id, request);
        ApiResponse<JobApplicationResponse> apiResponse = ApiResponse.<JobApplicationResponse>builder()
                .message("Job application updated successfully")
                .result(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        jobApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<JobApplicationResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<JobApplicationResponse> response = jobApplicationService.getAll(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<ApiResponse<PageResponse<JobApplicationResponse>>> getByCandidateId(
            @PathVariable UUID candidateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<JobApplicationResponse> response = jobApplicationService.getByCandidateId(candidateId, pageable);
        ApiResponse<PageResponse<JobApplicationResponse>> apiResponse = ApiResponse.<PageResponse<JobApplicationResponse>>builder()
                .result(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<PageResponse<JobApplicationResponse> >> getByJobId(
            @PathVariable UUID jobId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        PageResponse<JobApplicationResponse> response = jobApplicationService.getByJobId(jobId, pageable);
        ApiResponse<PageResponse<JobApplicationResponse> > apiResponse = ApiResponse.<PageResponse<JobApplicationResponse> >builder()
                .result(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PageResponse<JobApplicationResponse>>> getByStatus(
            @PathVariable JobApplication.ApplicationStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        PageResponse<JobApplicationResponse> response = jobApplicationService.getByStatus(status, pageable);
        ApiResponse<PageResponse<JobApplicationResponse>> apiResponse = ApiResponse.<PageResponse<JobApplicationResponse> >builder()
                .result(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> updateStatus(
            @PathVariable UUID id,
            @RequestParam JobApplication.ApplicationStatus status) {
        JobApplicationResponse response = jobApplicationService.updateStatus(id, status);
        ApiResponse<JobApplicationResponse> apiResponse = ApiResponse.<JobApplicationResponse>builder()
                .message("Update status")
                .result(response)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> checkExists(
            @RequestParam UUID candidateId,
            @RequestParam UUID jobId) {
        boolean exists = jobApplicationService.existsByCandiateAndJob(candidateId, jobId);
        ApiResponse<Boolean> apiResponse = ApiResponse.<Boolean>builder()
                .message("Check if job application exists")
                .result(exists)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<ApiResponse<Long>> getCountByStatus(@PathVariable JobApplication.ApplicationStatus status) {
        long count = jobApplicationService.getCountByStatus(status);
        ApiResponse<Long> apiResponse = ApiResponse.<Long>builder()
                .message("Count of job applications by status")
                .result(count)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
