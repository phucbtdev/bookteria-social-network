package com.recruitment.candidate_service.controller;

import com.recruitment.candidate_service.dto.request.SavedJobCreationRequest;
import com.recruitment.candidate_service.dto.response.SavedJobResponse;
import com.recruitment.candidate_service.service.SavedJobService;
import com.recruitment.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/saved-jobs")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SavedJobController {

    SavedJobService savedJobService;

    @PostMapping
    public ResponseEntity<ApiResponse<SavedJobResponse>> createSavedJob(@Valid @RequestBody SavedJobCreationRequest request) {
        SavedJobResponse response = savedJobService.createSavedJob(request);
        ApiResponse<SavedJobResponse> apiResponse = ApiResponse.<SavedJobResponse>builder()
                .code(1000)
                .message("Saved job created successfully")
                .result(response)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSavedJob(@PathVariable UUID id) {
        savedJobService.deleteSavedJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<SavedJobResponse>> getAllSavedJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "savedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<SavedJobResponse> savedJobs = savedJobService.getAllSavedJobs(pageable);
        return ResponseEntity.ok(savedJobs);
    }

    @GetMapping("/candidate/{candidate}")
    public ResponseEntity<Page<SavedJobResponse>> getSavedJobsByCandidate(
            @PathVariable UUID candidate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SavedJobResponse> savedJobs = savedJobService.getSavedJobsByCandidate(candidate, pageable);
        return ResponseEntity.ok(savedJobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SavedJobResponse> getSavedJobById(@PathVariable UUID id) {
        SavedJobResponse response = savedJobService.getSavedJobById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isJobSavedByCandidate(
            @RequestParam UUID candidate,
            @RequestParam UUID jobPostId) {
        boolean isSaved = savedJobService.isJobSavedByCandidate(candidate, jobPostId);
        return ResponseEntity.ok(isSaved);
    }
}
