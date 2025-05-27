package com.recruitment.candidate_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SavedJobCreationRequest {

    @NotNull(message = "Candidate ID is required")
    UUID candidateId;

    @NotNull(message = "Job Post ID is required")
    UUID jobPostId;

}
