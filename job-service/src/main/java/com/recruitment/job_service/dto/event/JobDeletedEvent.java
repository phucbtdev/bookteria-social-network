package com.recruitment.job_service.dto.event;

import java.util.UUID;

public record JobDeletedEvent(UUID id) {}