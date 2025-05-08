package com.recruitment.employer_service.event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerCreatedEvent {
    UUID userId;
    UUID employerId;
    String companyName;
}

