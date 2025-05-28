package com.recruitment.search_service.service;

import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.event.JobEvent;
import com.recruitment.search_service.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSearchService {

    private final JobRepository jobRepository;

    public void indexJob(JobEvent.JobData jobData) {
        log.info("Indexing job with ID: {}", jobData.getJobId());

        JobDocument jobDocument = mapToJobDocument(jobData);
        jobRepository.save(jobDocument);

        log.info("Successfully indexed job: {}", jobData.getJobId());
    }

    public void updateJob(JobEvent.JobData jobData) {
        log.info("Updating job with ID: {}", jobData.getJobId());

        Optional<JobDocument> existingJob = jobRepository.findByJobId(jobData.getJobId());
        if (existingJob.isPresent()) {
            JobDocument jobDocument = mapToJobDocument(jobData);
            jobDocument.setId(existingJob.get().getId()); // Keep the same document ID
            jobRepository.save(jobDocument);
            log.info("Successfully updated job: {}", jobData.getJobId());
        } else {
            log.warn("Job not found for update: {}", jobData.getJobId());
            // Create new document if not exists
            indexJob(jobData);
        }
    }

    public void updateJobStatus(UUID jobId, String status) {
        log.info("Updating job status for ID: {} to {}", jobId, status);

        Optional<JobDocument> existingJob = jobRepository.findByJobId(jobId);
        if (existingJob.isPresent()) {
            JobDocument jobDocument = existingJob.get();
            jobDocument.setStatus(status);
            jobRepository.save(jobDocument);
            log.info("Successfully updated job status: {}", jobId);
        } else {
            log.warn("Job not found for status update: {}", jobId);
        }
    }

    public void deleteJob(UUID jobId) {
        log.info("Deleting job with ID: {}", jobId);

        jobRepository.deleteByJobId(jobId);

        log.info("Successfully deleted job: {}", jobId);
    }

    public Page<JobDocument> searchJobs(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return jobRepository.findByStatus("APPROVED", pageable);
        }
        return jobRepository.searchActiveJobs(keyword.trim(), pageable);
    }

    public Page<JobDocument> searchJobsByIndustry(String keyword, UUID industryId, Pageable pageable) {
        return jobRepository.searchJobsByIndustry(keyword, industryId, pageable);
    }

    public Page<JobDocument> findJobsNearLocation(String keyword, Double distance,
                                                  Double latitude, Double longitude, Pageable pageable) {
        return jobRepository.findJobsNearLocation(keyword, distance, latitude, longitude, pageable);
    }

    public Page<JobDocument> findJobsBySkills(List<String> skills, Pageable pageable) {
        return jobRepository.findJobsBySkills(skills, pageable);
    }

    public Optional<JobDocument> findJobBySlug(String slug) {
        // This would require adding a method to repository
        return jobRepository.findAll().stream()
                .filter(job -> slug.equals(job.getSlug()))
                .findFirst();
    }

    public List<JobDocument> findJobsByEmployer(UUID employerId) {
        return jobRepository.findByEmployerId(employerId);
    }

    private JobDocument mapToJobDocument(JobEvent.JobData jobData) {
        return JobDocument.builder()
                .jobId(jobData.getJobId())
                .employerId(jobData.getEmployerId())
                .title(jobData.getTitle())
                .slug(jobData.getSlug())
                .description(jobData.getDescription())
                .industry(mapToIndustryInfo(jobData.getIndustry()))
                .jobLevel(mapToJobLevelInfo(jobData.getJobLevel()))
                .experienceLevel(mapToExperienceLevelInfo(jobData.getExperienceLevel()))
                .salaryRange(mapToSalaryRangeInfo(jobData.getSalaryRange()))
                .workType(mapToWorkTypeInfo(jobData.getWorkType()))
                .numberOfPositions(jobData.getNumberOfPositions())
                .skills(jobData.getSkills())
                .genderRequirement(jobData.getGenderRequirement())
                .address(jobData.getAddress())
                .location(JobDocument.GeoPoint.of(jobData.getLatitude(), jobData.getLongitude()))
                .applicationDeadline(jobData.getApplicationDeadline())
                .status(jobData.getStatus())
                .createdAt(jobData.getCreatedAt())
                .updatedAt(jobData.getUpdatedAt())
                .build();
    }

    private JobDocument.IndustryInfo mapToIndustryInfo(JobEvent.IndustryData industry) {
        if (industry == null) return null;
        return JobDocument.IndustryInfo.builder()
                .id(industry.getId())
                .name(industry.getName())
                .code(industry.getCode())
                .build();
    }

    private JobDocument.JobLevelInfo mapToJobLevelInfo(JobEvent.JobLevelData jobLevel) {
        if (jobLevel == null) return null;
        return JobDocument.JobLevelInfo.builder()
                .id(jobLevel.getId())
                .name(jobLevel.getName())
                .level(jobLevel.getLevel())
                .build();
    }

    private JobDocument.ExperienceLevelInfo mapToExperienceLevelInfo(JobEvent.ExperienceLevelData experienceLevel) {
        if (experienceLevel == null) return null;
        return JobDocument.ExperienceLevelInfo.builder()
                .id(experienceLevel.getId())
                .name(experienceLevel.getName())
                .minYears(experienceLevel.getMinYears())
                .maxYears(experienceLevel.getMaxYears())
                .build();
    }

    private JobDocument.SalaryRangeInfo mapToSalaryRangeInfo(JobEvent.SalaryRangeData salaryRange) {
        if (salaryRange == null) return null;
        return JobDocument.SalaryRangeInfo.builder()
                .id(salaryRange.getId())
                .name(salaryRange.getName())
                .minSalary(salaryRange.getMinSalary())
                .maxSalary(salaryRange.getMaxSalary())
                .currency(salaryRange.getCurrency())
                .build();
    }

    private JobDocument.WorkTypeInfo mapToWorkTypeInfo(JobEvent.WorkTypeData workType) {
        if (workType == null) return null;
        return JobDocument.WorkTypeInfo.builder()
                .id(workType.getId())
                .name(workType.getName())
                .type(workType.getType())
                .build();
    }
}