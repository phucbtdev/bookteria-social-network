package com.recruitment.job_service.service.implement;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.job_service.dto.event.JobCreatedEvent;
import com.recruitment.job_service.dto.event.JobDeletedEvent;
import com.recruitment.job_service.dto.event.JobUpdatedEvent;
import com.recruitment.job_service.dto.request.JobCreationRequest;
import com.recruitment.job_service.dto.request.JobFilterRequest;
import com.recruitment.job_service.dto.request.JobUpdateRequest;
import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.entity.*;
import com.recruitment.job_service.exception.AppException;
import com.recruitment.job_service.exception.ErrorCode;
import com.recruitment.job_service.mapper.JobMapper;
import com.recruitment.job_service.repository.*;
import com.recruitment.job_service.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final IndustryRepository industryRepository;
    private final JobLevelRepository jobLevelRepository;
    private final ExperienceLevelRepository experienceLevelRepository;
    private final SalaryRangeRepository salaryRangeRepository;
    private final WorkTypeRepository workTypeRepository;
    private final JobMapper jobMapper;
    private  final RabbitTemplate rabbitTemplate;

    @Override
    public JobResponse createJob(JobCreationRequest request) {
        log.info("Creating job with title: {}", request.getTitle());

        // Generate slug if not provided
        if (!StringUtils.hasText(request.getSlug())) {
            request.setSlug(generateSlug(request.getTitle()));
        }

        // Check if slug already exists
        if (jobRepository.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.SLUG_EXISTED);
        }

        Job job = jobMapper.toEntity(request);

        // Set related entities
        setJobRelatedEntities(job, request.getIndustryId(), request.getJobLevelId(),
                request.getExperienceLevelId(), request.getSalaryRangeId(),
                request.getWorkTypeId());

        Job savedJob = jobRepository.save(job);
        log.info("Job created successfully with ID: {}", savedJob.getId());

        JobCreatedEvent event = new JobCreatedEvent(
                savedJob.getId(),
                savedJob.getEmployerId(),
                savedJob.getTitle(),
                savedJob.getSlug(),
                savedJob.getDescription(),
                savedJob.getIndustry() != null ? savedJob.getIndustry().getId() : null,
                savedJob.getJobLevel() != null ? savedJob.getJobLevel().getId() : null,
                savedJob.getExperienceLevel() != null ? savedJob.getExperienceLevel().getId() : null,
                savedJob.getSalaryRange() != null ? savedJob.getSalaryRange().getId() : null,
                savedJob.getWorkType() != null ? savedJob.getWorkType().getId() : null,
                savedJob.getNumberOfPositions(),
                savedJob.getSkillsRequired(),
                savedJob.getGenderRequirement().name(),
                savedJob.getAddress(),
                savedJob.getApplicationDeadline(),
                savedJob.getStatus().name()
        );
        rabbitTemplate.convertAndSend("job.exchange", "job.created", event);

        return jobMapper.toResponse(savedJob);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobById(UUID id) {
        log.info("Fetching job with ID: {}", id);
        Job job = findJobById(id);
        return jobMapper.toResponse(job);
    }

    @Override
    @Transactional(readOnly = true)
    public JobResponse getJobBySlug(String slug) {
        log.info("Fetching job with slug: {}", slug);
        Job job = jobRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.SLUG_NOT_EXISTED));
        return jobMapper.toResponse(job);
    }

    @Override
    public JobResponse updateJob(UUID id, JobUpdateRequest request) {
        Job existingJob = findJobById(id);

        // Check slug uniqueness if it's being updated
        if (StringUtils.hasText(request.getSlug()) &&
                !request.getSlug().equals(existingJob.getSlug()) &&
                jobRepository.existsBySlug(request.getSlug())) {
            throw new AppException(ErrorCode.SLUG_EXISTED);
        }

        jobMapper.updateEntityFromRequest(request, existingJob);

        // Update related entities if provided
        setJobRelatedEntities(existingJob, request.getIndustryId(), request.getJobLevelId(),
                request.getExperienceLevelId(), request.getSalaryRangeId(),
                request.getWorkTypeId());

        Job updatedJob = jobRepository.save(existingJob);
        log.info("Job updated successfully with ID: {}", updatedJob.getId());

        JobUpdatedEvent event = new JobUpdatedEvent(
                updatedJob.getId(),
                updatedJob.getTitle(),
                updatedJob.getSlug(),
                updatedJob.getDescription(),
                updatedJob.getIndustry() != null ? updatedJob.getIndustry().getId() : null,
                updatedJob.getJobLevel() != null ? updatedJob.getJobLevel().getId() : null,
                updatedJob.getExperienceLevel() != null ? updatedJob.getExperienceLevel().getId() : null,
                updatedJob.getSalaryRange() != null ? updatedJob.getSalaryRange().getId() : null,
                updatedJob.getWorkType() != null ? updatedJob.getWorkType().getId() : null,
                updatedJob.getNumberOfPositions(),
                updatedJob.getSkillsRequired(),
                updatedJob.getGenderRequirement().name(),
                updatedJob.getAddress(),
                updatedJob.getApplicationDeadline(),
                updatedJob.getStatus().name()
        );
        rabbitTemplate.convertAndSend("job.exchange", "job.updated", event);

        return jobMapper.toResponse(updatedJob);
    }

    @Override
    public void deleteJob(UUID id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found");
        }
        jobRepository.deleteById(id);
        JobDeletedEvent event = new JobDeletedEvent(id);
        rabbitTemplate.convertAndSend("job.exchange", "job.deleted", event);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobResponse> getAllJobs(int page, int size, String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        var pageData = jobRepository.findAll(pageable);
        var postList = pageData.getContent().stream().map(jobMapper::toResponse).toList();

        return PageResponse.<JobResponse>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(postList)
                .last(pageData.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobResponse> getJobsByEmployer(UUID employerId, int page, int size,
                                               String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        Page<Job> jobs = jobRepository.findByEmployerId(employerId, pageable);
        List<JobResponse> dataList = jobs.getContent().stream().map(jobMapper::toResponse).toList();

        return PageResponse.<JobResponse>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(jobs.getTotalPages())
                .totalElements(jobs.getTotalElements())
                .data(dataList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<JobResponse> searchJobs(JobFilterRequest filterRequest) {
        log.info("Searching jobs with filters: {}", filterRequest);

        Pageable pageable = createPageable(filterRequest.getPage(), filterRequest.getSize(),
                filterRequest.getSortBy(), filterRequest.getSortDirection());

        Page<Job> jobs = jobRepository.findJobsWithFilters(
                filterRequest.getTitle(),
                filterRequest.getIndustryId(),
                filterRequest.getJobLevelId(),
                filterRequest.getExperienceLevelId(),
                filterRequest.getWorkTypeId(),
                filterRequest.getGenderRequirement(),
                filterRequest.getStatus(),
                filterRequest.getDeadlineFrom(),
                filterRequest.getDeadlineTo(),
                filterRequest.getLocation(),
                pageable
        );

        List<JobResponse> jobResponses = jobs.getContent().stream()
                .map(jobMapper::toResponse)
                .toList();

        return PageResponse.<JobResponse>builder()
                .pageNo(filterRequest.getPage())
                .pageSize(filterRequest.getSize())
                .totalPages(jobs.getTotalPages())
                .totalElements(jobs.getTotalElements())
                .data(jobResponses)
                .last(jobs.isLast())
                .build();
    }

    @Override
    public JobResponse approveJob(UUID id) {
        log.info("Approving job with ID: {}", id);
        Job job = findJobById(id);
        job.setStatus(Job.JobPostStatus.APPROVED);
        Job approvedJob = jobRepository.save(job);
        log.info("Job approved successfully with ID: {}", id);
        return jobMapper.toResponse(approvedJob);
    }

    @Override
    public JobResponse rejectJob(UUID id) {
        log.info("Rejecting job with ID: {}", id);
        Job job = findJobById(id);
        job.setStatus(Job.JobPostStatus.REJECTED);
        Job rejectedJob = jobRepository.save(job);
        log.info("Job rejected successfully with ID: {}", id);
        return jobMapper.toResponse(rejectedJob);
    }

    // Helper methods
    private Job findJobById(UUID id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.JOB_NOT_EXISTED));
    }

    private void setJobRelatedEntities(Job job, UUID industryId, UUID jobLevelId,
                                       UUID experienceLevelId, UUID salaryRangeId, UUID workTypeId) {
        if (industryId != null) {
            Industry industry = industryRepository.findById(industryId)
                    .orElseThrow(() -> new AppException(ErrorCode.INDUSTRY_NOT_EXISTED));
            job.setIndustry(industry);
        }

        if (jobLevelId != null) {
            JobLevel jobLevel = jobLevelRepository.findById(jobLevelId)
                    .orElseThrow(() -> new AppException(ErrorCode.JOB_LEVEL_NOT_EXISTED));
            job.setJobLevel(jobLevel);
        }

        if (experienceLevelId != null) {
            ExperienceLevel experienceLevel = experienceLevelRepository.findById(experienceLevelId)
                    .orElseThrow(() -> new AppException(ErrorCode.EXPERIENCE_LEVEL_NOT_EXISTED));
            job.setExperienceLevel(experienceLevel);
        }

        if (salaryRangeId != null) {
            SalaryRange salaryRange = salaryRangeRepository.findById(salaryRangeId)
                    .orElseThrow(() -> new AppException(ErrorCode.SALARY_RANGE_NOT_EXISTED));
            job.setSalaryRange(salaryRange);
        }

        if (workTypeId != null) {
            WorkType workType = workTypeRepository.findById(workTypeId)
                    .orElseThrow(() -> new AppException(ErrorCode.WORK_TYPE_NOT_EXISTED));
            job.setWorkType(workType);
        }
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return PageRequest.of(page - 1, size, sort);
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("(^-)|(-$)", "");
    }
}
