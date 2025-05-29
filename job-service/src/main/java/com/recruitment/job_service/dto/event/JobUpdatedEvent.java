package com.recruitment.job_service.dto.event;

import java.time.LocalDate;
import java.util.UUID;
public record JobUpdatedEvent(
        UUID id,
        String title,
        String slug,
        String description,
        UUID industryId,
        UUID jobLevelId,
        UUID experienceLevelId,
        UUID salaryRangeId,
        UUID workTypeId,
        Integer numberOfPositions,
        String skillsRequired,
        String genderRequirement,
        String address,
        LocalDate applicationDeadline,
        String status
) {}