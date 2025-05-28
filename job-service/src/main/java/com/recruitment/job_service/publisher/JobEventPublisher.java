package com.recruitment.job_service.publisher;

import com.recruitment.job_service.entity.Job;
import com.recruitment.job_service.event.JobEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.job-events}")
    private String jobEventsExchange;

    @Value("${rabbitmq.routing-key.job-events}")
    private String jobEventsRoutingKey;

    public void publishJobCreated(Job job) {
        JobEvent event = createJobEvent(job, JobEvent.EventType.JOB_CREATED);
        publishEvent(event, "job.created");
        log.info("Published job created event for job: {}", job.getId());
    }

    public void publishJobUpdated(Job job) {
        JobEvent event = createJobEvent(job, JobEvent.EventType.JOB_UPDATED);
        publishEvent(event, "job.updated");
        log.info("Published job updated event for job: {}", job.getId());
    }

    public void publishJobDeleted(Job job) {
        JobEvent event = JobEvent.builder()
                .eventType(JobEvent.EventType.JOB_DELETED)
                .jobId(job.getId())
                .eventTime(LocalDateTime.now())
                .build();
        publishEvent(event, "job.deleted");
        log.info("Published job deleted event for job: {}", job.getId());
    }

    public void publishJobStatusChanged(Job job) {
        JobEvent event = createJobEvent(job, JobEvent.EventType.JOB_STATUS_CHANGED);
        publishEvent(event, "job.status.changed");
        log.info("Published job status changed event for job: {} to status: {}",
                job.getId(), job.getStatus());
    }

    private JobEvent createJobEvent(Job job, JobEvent.EventType eventType) {
        return JobEvent.builder()
                .eventType(eventType)
                .jobId(job.getId())
                .jobData(mapToJobData(job))
                .eventTime(LocalDateTime.now())
                .build();
    }

    private void publishEvent(JobEvent event, String routingKey) {
        try {
            rabbitTemplate.convertAndSend(jobEventsExchange, routingKey, event);
            log.debug("Successfully published event: {} with routing key: {}", event.getEventType(), routingKey);
        } catch (Exception e) {
            log.error("Failed to publish event: {} with routing key: {}. Error: {}",
                    event.getEventType(), routingKey, e.getMessage(), e);
            throw new RuntimeException("Failed to publish job event", e);
        }
    }

    private JobEvent.JobData mapToJobData(Job job) {
        return JobEvent.JobData.builder()
                .jobId(job.getId())
                .employerId(job.getEmployerId())
                .title(job.getTitle())
                .slug(job.getSlug())
                .description(job.getDescription())
                .industry(mapToIndustryData(job.getIndustry()))
                .jobLevel(mapToJobLevelData(job.getJobLevel()))
                .experienceLevel(mapToExperienceLevelData(job.getExperienceLevel()))
                .salaryRange(mapToSalaryRangeData(job.getSalaryRange()))
                .workType(mapToWorkTypeData(job.getWorkType()))
                .numberOfPositions(job.getNumberOfPositions())
                .skills(parseSkills(job.getSkillsRequired()))
                .genderRequirement(job.getGenderRequirement() != null ?
                        job.getGenderRequirement().toString() : "ANY")
                .address(job.getAddress())
                .latitude(job.getLatitude())
                .longitude(job.getLongitude())
                .applicationDeadline(job.getApplicationDeadline())
                .status(job.getStatus() != null ? job.getStatus().toString() : "PENDING")
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }

    private List<String> parseSkills(String skillsRequired) {
        if (skillsRequired == null || skillsRequired.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(skillsRequired.split("[,;\\n]"))
                .stream()
                .map(String::trim)
                .filter(skill -> !skill.isEmpty())
                .toList();
    }

    private JobEvent.IndustryData mapToIndustryData(Object industry) {
        if (industry == null) return null;

        try {
            // Assuming industry has getId(), getName(), getCode() methods
            // You may need to cast to the actual Industry type
            return JobEvent.IndustryData.builder()
                    .id((java.util.UUID) getFieldValue(industry, "getId"))
                    .name((String) getFieldValue(industry, "getName"))
                    .code((String) getFieldValue(industry, "getCode"))
                    .build();
        } catch (Exception e) {
            log.warn("Failed to map industry data: {}", e.getMessage());
            return null;
        }
    }

    private JobEvent.JobLevelData mapToJobLevelData(Object jobLevel) {
        if (jobLevel == null) return null;

        try {
            // Assuming jobLevel has getId(), getName(), getLevel() methods
            return JobEvent.JobLevelData.builder()
                    .id((java.util.UUID) getFieldValue(jobLevel, "getId"))
                    .name((String) getFieldValue(jobLevel, "getName"))
                    .level((Integer) getFieldValue(jobLevel, "getLevel"))
                    .build();
        } catch (Exception e) {
            log.warn("Failed to map job level data: {}", e.getMessage());
            return null;
        }
    }

    private JobEvent.ExperienceLevelData mapToExperienceLevelData(Object experienceLevel) {
        if (experienceLevel == null) return null;

        try {
            // Assuming experienceLevel has getId(), getName(), getMinYears(), getMaxYears() methods
            return JobEvent.ExperienceLevelData.builder()
                    .id((java.util.UUID) getFieldValue(experienceLevel, "getId"))
                    .name((String) getFieldValue(experienceLevel, "getName"))
                    .minYears((Integer) getFieldValue(experienceLevel, "getMinYears"))
                    .maxYears((Integer) getFieldValue(experienceLevel, "getMaxYears"))
                    .build();
        } catch (Exception e) {
            log.warn("Failed to map experience level data: {}", e.getMessage());
            return null;
        }
    }

    private JobEvent.SalaryRangeData mapToSalaryRangeData(Object salaryRange) {
        if (salaryRange == null) return null;

        try {
            // Assuming salaryRange has getId(), getName(), getMinSalary(), getMaxSalary(), getCurrency() methods
            return JobEvent.SalaryRangeData.builder()
                    .id((java.util.UUID) getFieldValue(salaryRange, "getId"))
                    .name((String) getFieldValue(salaryRange, "getName"))
                    .minSalary((Long) getFieldValue(salaryRange, "getMinSalary"))
                    .maxSalary((Long) getFieldValue(salaryRange, "getMaxSalary"))
                    .currency((String) getFieldValue(salaryRange, "getCurrency"))
                    .build();
        } catch (Exception e) {
            log.warn("Failed to map salary range data: {}", e.getMessage());
            return null;
        }
    }

    private JobEvent.WorkTypeData mapToWorkTypeData(Object workType) {
        if (workType == null) return null;

        try {
            // Assuming workType has getId(), getName(), getType() methods
            return JobEvent.WorkTypeData.builder()
                    .id((java.util.UUID) getFieldValue(workType, "getId"))
                    .name((String) getFieldValue(workType, "getName"))
                    .type((String) getFieldValue(workType, "getType"))
                    .build();
        } catch (Exception e) {
            log.warn("Failed to map work type data: {}", e.getMessage());
            return null;
        }
    }

    private Object getFieldValue(Object obj, String methodName) {
        try {
            java.lang.reflect.Method method = obj.getClass().getMethod(methodName);
            return method.invoke(obj);
        } catch (Exception e) {
            log.debug("Failed to get field value using method: {} from object: {}", methodName, obj.getClass().getSimpleName());
            throw new RuntimeException("Failed to get field value", e);
        }
    }
}