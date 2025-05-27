package com.recruitment.job_service.dto.request;

import com.recruitment.job_service.entity.JobApplication;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobApplicationUpdateRequest {

    @Size(max = 5000, message = "Cover letter must not exceed 5000 characters")
    String coverLetter;

    @Size(max = 500, message = "Resume URL must not exceed 500 characters")
    String resumeUrl;

    JobApplication.ApplicationStatus status;
}
