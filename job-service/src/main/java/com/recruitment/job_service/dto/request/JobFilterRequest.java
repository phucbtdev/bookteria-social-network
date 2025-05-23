package com.recruitment.job_service.dto.request;

import com.recruitment.job_service.entity.Job;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobFilterRequest {
    String title;
    UUID industryId;
    UUID jobLevelId;
    UUID experienceLevelId;
    UUID workTypeId;
    Job.GenderRequirement genderRequirement;
    Job.JobPostStatus status;
    LocalDate deadlineFrom;
    LocalDate deadlineTo;
    String location;
    @Builder.Default
    Integer page = 1;
    @Builder.Default
    Integer size = 10;
    @Builder.Default
    String sortBy = "createdAt";
    @Builder.Default
    String sortDirection = "DESC";
}
