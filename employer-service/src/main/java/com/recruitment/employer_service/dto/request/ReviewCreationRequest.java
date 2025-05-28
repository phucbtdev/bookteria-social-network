package com.recruitment.employer_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewCreationRequest {
    @NotNull(message = "Candidate ID không được để trống")
    UUID candidateId;

    @NotNull(message = "Employer ID không được để trống")
    UUID employerId;

    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải từ 1 đến 5")
    @Max(value = 5, message = "Rating phải từ 1 đến 5")
    Integer rating;

    @Size(max = 1000, message = "Nội dung đánh giá không được vượt quá 1000 ký tự")
    String reviewText;
}
