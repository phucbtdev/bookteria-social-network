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
    Integer page = 0;
    Integer size = 10;
    String sortBy = "createdAt";
    String sortDirection = "DESC";
}
