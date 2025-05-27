package com.recruitment.job_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobApplicationCreateRequest {

    @NotNull(message = "Candidate ID is required")
    UUID candidateId;

    @NotNull(message = "Job ID is required")
    UUID jobId;

    @Size(max = 5000, message = "Cover letter must not exceed 5000 characters")
    String coverLetter;

    @Size(max = 500, message = "Resume URL must not exceed 500 characters")
    String resumeUrl;
}
