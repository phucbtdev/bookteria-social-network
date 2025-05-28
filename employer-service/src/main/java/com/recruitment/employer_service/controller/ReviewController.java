package com.recruitment.employer_service.controller;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.employer_service.dto.request.ReviewCreationRequest;
import com.recruitment.employer_service.dto.response.ReviewResponse;
import com.recruitment.employer_service.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewCreationRequest request) {
        ReviewResponse response = reviewService.createReview(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable UUID id) {
        ReviewResponse response = reviewService.getReviewById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReviewResponse>> getAllReviews(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {

        PageResponse<ReviewResponse> response = reviewService.getAllReviews(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employer/{employerId}")
    public ResponseEntity<PageResponse<ReviewResponse>> getReviewsByEmployerId(
            @PathVariable UUID employerId,
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {

        PageResponse<ReviewResponse> response = reviewService.getReviewsByEmployerId(employerId, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<PageResponse<ReviewResponse>> getReviewsByCandidateId(
            @PathVariable UUID candidateId,
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {

        PageResponse<ReviewResponse> response = reviewService.getReviewsByCandidateId(candidateId, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employer/{employerId}/average-rating")
    public ResponseEntity<Double> getAverageRatingByEmployerId(@PathVariable UUID employerId) {
        Double averageRating = reviewService.getAverageRatingByEmployerId(employerId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/employer/{employerId}/total-reviews")
    public ResponseEntity<Long> getTotalReviewsByEmployerId(@PathVariable UUID employerId) {
        Long totalReviews = reviewService.getTotalReviewsByEmployerId(employerId);
        return ResponseEntity.ok(totalReviews);
    }
}
