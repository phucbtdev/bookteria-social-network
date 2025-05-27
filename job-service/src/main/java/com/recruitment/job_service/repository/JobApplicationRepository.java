package com.recruitment.job_service.repository;

import com.recruitment.job_service.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, UUID> {

    // Find by candidate ID with pagination
    Page<JobApplication> findByCandidateId(UUID candidateId, Pageable pageable);

    // Find by job ID with pagination
    @Query("SELECT ja FROM JobApplication ja WHERE ja.job.id = :jobId")
    Page<JobApplication> findByJobId(@Param("jobId") UUID jobId, Pageable pageable);

    // Find by status with pagination
    Page<JobApplication> findByStatus(JobApplication.ApplicationStatus status, Pageable pageable);

    // Find by candidate and job
    Optional<JobApplication> findByCandidateIdAndJobId(UUID candidateId, UUID jobId);

    // Find by candidate and status
    List<JobApplication> findByCandidateIdAndStatus(UUID candidateId, JobApplication.ApplicationStatus status);

    // Count applications by status
    long countByStatus(JobApplication.ApplicationStatus status);

    // Check if application exists
    boolean existsByCandidateIdAndJobId(UUID candidateId, UUID jobId);

    // Custom query with join fetch
    @Query("SELECT ja FROM JobApplication ja JOIN FETCH ja.job WHERE ja.id = :id")
    Optional<JobApplication> findByIdWithJob(@Param("id") UUID id);

    // Find with job details
    @Query("SELECT ja FROM JobApplication ja JOIN FETCH ja.job")
    Page<JobApplication> findAllWithJob(Pageable pageable);
}
