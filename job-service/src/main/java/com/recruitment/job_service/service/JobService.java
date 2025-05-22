package com.recruitment.job_service.service;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.job_service.dto.request.JobCreationRequest;
import com.recruitment.job_service.dto.request.JobFilterRequest;
import com.recruitment.job_service.dto.request.JobUpdateRequest;
import com.recruitment.job_service.dto.response.JobResponse;

import java.util.UUID;

public interface JobService {

    JobResponse createJob(JobCreationRequest request);

    JobResponse getJobById(UUID id);

    JobResponse getJobBySlug(String slug);

    JobResponse updateJob(UUID id, JobUpdateRequest request);

    void deleteJob(UUID id);

    PageResponse<JobResponse> getAllJobs(int page, int size, String sortBy, String sortDirection);

    PageResponse<JobResponse> getJobsByEmployer(UUID employerId, int page, int size, String sortBy, String sortDirection);

    PageResponse<JobResponse> searchJobs(JobFilterRequest filterRequest);

    JobResponse approveJob(UUID id);

    JobResponse rejectJob(UUID id);
}

