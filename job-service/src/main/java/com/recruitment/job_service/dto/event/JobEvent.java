package com.recruitment.job_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobEvent {
        private UUID id;
        private UUID employerId;
        private String title;
        private String slug;
        private String description;
        private String industryName;
        private String jobLevel;
        private String experienceLevel;
        private String salaryRange;
        private String workType;
        private Integer numberOfPositions;
        private String skillsRequired;
        private String genderRequirement;
        private String address;
        private String status;
}
