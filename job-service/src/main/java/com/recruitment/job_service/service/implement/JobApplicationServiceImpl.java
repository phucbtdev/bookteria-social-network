package com.recruitment.job_service.service.implement;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.job_service.dto.request.JobApplicationCreateRequest;
import com.recruitment.job_service.dto.request.JobApplicationUpdateRequest;
import com.recruitment.job_service.dto.response.JobApplicationResponse;
import com.recruitment.job_service.entity.Job;
import com.recruitment.job_service.entity.JobApplication;
import com.recruitment.job_service.exception.AppException;
import com.recruitment.job_service.exception.ErrorCode;
import com.recruitment.job_service.mapper.JobApplicationMapper;
import com.recruitment.job_service.repository.JobApplicationRepository;
import com.recruitment.job_service.repository.JobRepository;
import com.recruitment.job_service.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final JobApplicationMapper jobApplicationMapper;

    @Override
    public JobApplicationResponse create(JobApplicationCreateRequest request) {
        log.info("Creating job application for candidate: {} and job: {}",
                request.getCandidateId(), request.getJobId());

        // Check if application already exists
        if (jobApplicationRepository.existsByCandidateIdAndJobId(
                request.getCandidateId(), request.getJobId())) {
            throw new AppException(ErrorCode.APPLICATION_ALREADY_EXISTS);
        }

        // Verify job exists
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new AppException(ErrorCode.JOB_APPLICATION_NOT_EXISTED));

        JobApplication entity = jobApplicationMapper.toEntity(request);
        entity.setJob(job);

        JobApplication saved = jobApplicationRepository.save(entity);
        log.info("Created job application with ID: {}", saved.getId());

        return jobApplicationMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse getById(UUID id) {
        log.info("Getting job application by ID: {}", id);

        JobApplication entity = jobApplicationRepository.findByIdWithJob(id)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_APPLICATION_NOT_EXISTED));

        return jobApplicationMapper.toResponse(entity);
    }

    @Override
    public JobApplicationResponse update(UUID id, JobApplicationUpdateRequest request) {
        log.info("Updating job application with ID: {}", id);

        JobApplication entity = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_APPLICATION_NOT_EXISTED));

        jobApplicationMapper.updateEntity(entity, request);
        JobApplication updated = jobApplicationRepository.save(entity);

        log.info("Updated job application with ID: {}", id);
        return jobApplicationMapper.toResponse(updated);
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting job application with ID: {}", id);

        if (!jobApplicationRepository.existsById(id)) {
            throw new AppException(ErrorCode.JOB_APPLICATION_NOT_EXISTED);
        }

        jobApplicationRepository.deleteById(id);
        log.info("Deleted job application with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobApplicationResponse> getAll(Pageable pageable) {

        Page<JobApplication> jobApplications = jobApplicationRepository.findAllWithJob(pageable);

        List<JobApplicationResponse> jobApplicationResponses = jobApplications.getContent().stream().map(jobApplicationMapper::toResponse).toList();

        return PageResponse.<JobApplicationResponse>builder()
                .pageNo(jobApplications.getNumber())
                .pageSize(jobApplications.getSize())
                .totalElements(jobApplications.getNumberOfElements())
                .totalPages(jobApplications.getTotalPages())
                .data(jobApplicationResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobApplicationResponse> getByCandidateId(UUID candidateId, Pageable pageable) {
        Page<JobApplication> jobApplications = jobApplicationRepository.findByCandidateId(candidateId, pageable);
        List<JobApplicationResponse> jobApplicationResponses = jobApplications.getContent().stream().map(jobApplicationMapper::toResponse).toList();

        return PageResponse.<JobApplicationResponse>builder()
                .pageNo(jobApplications.getNumber() + 1)
                .pageSize(jobApplications.getSize())
                .totalElements(jobApplications.getNumberOfElements())
                .totalPages(jobApplications.getTotalPages())
                .data(jobApplicationResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobApplicationResponse> getByJobId(UUID jobId, Pageable pageable) {
        Page<JobApplication> jobApplications = jobApplicationRepository.findByJobId(jobId, pageable);
        List<JobApplicationResponse> jobApplicationResponses = jobApplications.getContent().stream().map(jobApplicationMapper::toResponse).toList();

        return PageResponse.<JobApplicationResponse>builder()
                .pageNo(jobApplications.getNumber() + 1)
                .pageSize(jobApplications.getSize())
                .totalElements(jobApplications.getNumberOfElements())
                .totalPages(jobApplications.getTotalPages())
                .data(jobApplicationResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobApplicationResponse> getByStatus(JobApplication.ApplicationStatus status, Pageable pageable) {
        Page<JobApplication> jobApplications = jobApplicationRepository.findByStatus(status, pageable);
        List<JobApplicationResponse> jobApplicationResponses = jobApplications.getContent().stream().map(jobApplicationMapper::toResponse).toList();

        return PageResponse.<JobApplicationResponse>builder()
                .pageNo(jobApplications.getNumber() + 1)
                .pageSize(jobApplications.getSize())
                .totalElements(jobApplications.getNumberOfElements())
                .totalPages(jobApplications.getTotalPages())
                .data(jobApplicationResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCandiateAndJob(UUID candidateId, UUID jobId) {
        return jobApplicationRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }

    @Override
    public JobApplicationResponse updateStatus(UUID id, JobApplication.ApplicationStatus status) {
        log.info("Updating status for job application: {} to: {}", id, status);

        JobApplication entity = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_APPLICATION_NOT_EXISTED));

        entity.setStatus(status);
        JobApplication updated = jobApplicationRepository.save(entity);

        log.info("Updated status for job application: {}", id);
        return jobApplicationMapper.toResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountByStatus(JobApplication.ApplicationStatus status) {
        return jobApplicationRepository.countByStatus(status);
    }
}