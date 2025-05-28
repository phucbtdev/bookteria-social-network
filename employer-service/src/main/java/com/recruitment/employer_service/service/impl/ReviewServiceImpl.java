package com.recruitment.employer_service.service.impl;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.employer_service.dto.request.ReviewCreationRequest;
import com.recruitment.employer_service.dto.response.ReviewResponse;
import com.recruitment.employer_service.entity.Employer;
import com.recruitment.employer_service.entity.Review;
import com.recruitment.employer_service.exception.AppException;
import com.recruitment.employer_service.exception.ErrorCode;
import com.recruitment.employer_service.mapper.ReviewMapper;
import com.recruitment.employer_service.repository.EmployerRepository;
import com.recruitment.employer_service.repository.ReviewRepository;
import com.recruitment.employer_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EmployerRepository employerRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponse createReview(ReviewCreationRequest request) {
        Employer employer = employerRepository.findById(request.getEmployerId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYER_NOT_FOUND));

        Review review = reviewMapper.toEntity(request);
        review.setEmployer(employer);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toResponse(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        return reviewMapper.toResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getAllReviews(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Review> reviews = reviewRepository.findAll(pageable);

        return mapToPageResponse(reviews);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getReviewsByEmployerId(UUID employerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Review> reviews = reviewRepository.findByEmployerId(employerId, pageable);
        if (reviews.isEmpty()) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }

        return mapToPageResponse(reviews);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReviewResponse> getReviewsByCandidateId(UUID candidateId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Review> reviews = reviewRepository.findByCandidateId(candidateId, pageable);

        return mapToPageResponse(reviews);
    }

    @Override
    public void deleteReview(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingByEmployerId(UUID employerId) {
        if (!employerRepository.existsById(employerId)) {
            throw new AppException(ErrorCode.EMPLOYER_NOT_FOUND);
        }
        return reviewRepository.getAverageRatingByEmployerId(employerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalReviewsByEmployerId(UUID employerId) {
        if (!employerRepository.existsById(employerId)) {
            throw new AppException(ErrorCode.EMPLOYER_NOT_FOUND);
        }
        return reviewRepository.countReviewsByEmployerId(employerId);
    }

    private PageResponse<ReviewResponse> mapToPageResponse(Page<Review> reviews) {
        List<ReviewResponse> content = reviews.getContent()
                .stream()
                .map(reviewMapper::toResponse)
                .toList();

        PageResponse<ReviewResponse> pageResponse = new PageResponse<>();
        pageResponse.setPageNo(reviews.getNumber() + 1);
        pageResponse.setPageSize(reviews.getSize());
        pageResponse.setTotalElements(reviews.getTotalElements());
        pageResponse.setTotalPages(reviews.getTotalPages());
        pageResponse.setLast(reviews.isLast());
        pageResponse.setData(content);

        return pageResponse;
    }
}
