package com.recruitment.employer_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewResponse {
    UUID id;
    UUID candidateId;
    UUID employerId;
    String employerName;
    Integer rating;
    String reviewText;
    LocalDateTime createdAt;
}
