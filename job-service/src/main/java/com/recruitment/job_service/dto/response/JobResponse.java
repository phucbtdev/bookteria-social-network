package com.recruitment.job_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobResponse {
    String id;
    String content;
    String userId;
    String username;
    Instant createdDate;
    Instant modifiedDate;
}
