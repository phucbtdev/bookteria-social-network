package com.recruitment.job_service.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobEvent {
    EventType eventType;
    UUID jobId;
    JobData jobData;
    LocalDateTime eventTime;

    public enum EventType {
        JOB_CREATED,
        JOB_UPDATED,
        JOB_DELETED,
        JOB_STATUS_CHANGED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class JobData {
        UUID jobId;
        UUID employerId;
        String title;
        String slug;
        String description;
        IndustryData industry;
        JobLevelData jobLevel;
        ExperienceLevelData experienceLevel;
        SalaryRangeData salaryRange;
        WorkTypeData workType;
        Integer numberOfPositions;
        List<String> skills;
        String genderRequirement;
        String address;
        BigDecimal latitude;
        BigDecimal longitude;
        LocalDate applicationDeadline;
        String status;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndustryData {
        UUID id;
        String name;
        String code;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobLevelData {
        UUID id;
        String name;
        Integer level;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceLevelData {
        UUID id;
        String name;
        Integer minYears;
        Integer maxYears;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalaryRangeData {
        UUID id;
        String name;
        Long minSalary;
        Long maxSalary;
        String currency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkTypeData {
        UUID id;
        String name;
        String type;
    }
}