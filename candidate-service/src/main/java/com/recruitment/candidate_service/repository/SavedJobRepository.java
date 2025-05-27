package com.recruitment.candidate_service.repository;

import com.recruitment.candidate_service.entity.SavedJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, UUID> {

    boolean existsByCandidateIdAndJobPostId(UUID candidate, UUID jobPostId);

    @Query("SELECT sj FROM SavedJob sj WHERE sj.candidate = :candidate ORDER BY sj.savedAt DESC")
    Page<SavedJob> findByCandidateOrderBySavedAtDesc(@Param("candidate") UUID candidate, Pageable pageable);
}
