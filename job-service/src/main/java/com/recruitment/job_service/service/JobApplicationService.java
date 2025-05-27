package com.recruitment.job_service.service;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.job_service.dto.request.JobApplicationCreateRequest;
import com.recruitment.job_service.dto.request.JobApplicationUpdateRequest;
import com.recruitment.job_service.dto.response.JobApplicationResponse;
import com.recruitment.job_service.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobApplicationService {

    /**
     * Create a new job application
     */
    JobApplicationResponse create(JobApplicationCreateRequest request);

    /**
     * Get job application by ID
     */
    JobApplicationResponse getById(UUID id);

    /**
     * Update job application
     */
    JobApplicationResponse update(UUID id, JobApplicationUpdateRequest request);

    /**
     * Delete job application
     */
    void delete(UUID id);

    /**
     * Get all job applications with pagination
     */
    PageResponse<JobApplicationResponse> getAll(Pageable pageable);

    /**
     * Get job applications by candidate ID with pagination
     */
    PageResponse<JobApplicationResponse> getByCandidateId(UUID candidateId, Pageable pageable);

    /**
     * Get job applications by job ID with pagination
     */
    PageResponse<JobApplicationResponse> getByJobId(UUID jobId, Pageable pageable);

    /**
     * Get job applications by status with pagination
     */
    PageResponse<JobApplicationResponse> getByStatus(JobApplication.ApplicationStatus status, Pageable pageable);

    /**
     * Check if application already exists for candidate and job
     */
    boolean existsByCandiateAndJob(UUID candidateId, UUID jobId);

    /**
     * Update application status
     */
    JobApplicationResponse updateStatus(UUID id, JobApplication.ApplicationStatus status);

    /**
     * Get application count by status
     */
    long getCountByStatus(JobApplication.ApplicationStatus status);
}
