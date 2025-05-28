package com.recruitment.employer_service.repository;

import com.recruitment.employer_service.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findByEmployerId(UUID employerId, Pageable pageable);

    Page<Review> findByCandidateId(UUID candidateId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.employer.id = :employerId")
    Double getAverageRatingByEmployerId(@Param("employerId") UUID employerId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.employer.id = :employerId")
    Long countReviewsByEmployerId(@Param("employerId") UUID employerId);

    List<Review> findByEmployerIdAndRating(UUID employerId, Integer rating);
}
