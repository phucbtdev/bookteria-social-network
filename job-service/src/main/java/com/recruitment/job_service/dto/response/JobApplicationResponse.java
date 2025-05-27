package com.recruitment.job_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.recruitment.job_service.entity.JobApplication;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobApplicationResponse {

    UUID id;
    UUID candidateId;
    JobResponse job;
    String coverLetter;
    String resumeUrl;
    JobApplication.ApplicationStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime appliedAt;
}
