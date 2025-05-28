package com.recruitment.employer_service.service;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.employer_service.dto.request.ReviewCreationRequest;
import com.recruitment.employer_service.dto.response.ReviewResponse;

import java.util.UUID;

public interface ReviewService {

    ReviewResponse createReview(ReviewCreationRequest request);

    ReviewResponse getReviewById(UUID id);

    PageResponse<ReviewResponse> getAllReviews(int pageNo, int pageSize, String sortBy, String sortDir);

    PageResponse<ReviewResponse> getReviewsByEmployerId(UUID employerId, int pageNo, int pageSize, String sortBy, String sortDir);

    PageResponse<ReviewResponse> getReviewsByCandidateId(UUID candidateId, int pageNo, int pageSize, String sortBy, String sortDir);

    void deleteReview(UUID id);

    Double getAverageRatingByEmployerId(UUID employerId);

    Long getTotalReviewsByEmployerId(UUID employerId);
}
