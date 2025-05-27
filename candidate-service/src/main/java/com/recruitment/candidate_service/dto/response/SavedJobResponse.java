package com.recruitment.candidate_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SavedJobResponse {
    UUID id;
    UUID candidateId;
    UUID jobPostId;
    LocalDateTime savedAt;
}
