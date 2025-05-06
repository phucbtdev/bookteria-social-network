package com.recruitment.candidate_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CandidatePackageCreationRequest {
    @NotBlank(message = "Tên gói không được để trống")
    @Size(min = 3, max = 255, message = "Tên gói phải có từ 3 đến 255 ký tự")
    String name;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    String description;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Giá không hợp lệ (tối đa 10 chữ số phần nguyên và 2 chữ số phần thập phân)")
    BigDecimal price;

    @NotNull(message = "Thời hạn (ngày) không được để trống")
    @Min(value = 1, message = "Thời hạn phải ít nhất là 1 ngày")
    @Max(value = 3650, message = "Thời hạn không được vượt quá 3650 ngày (10 năm)")
    Integer durationDays;

    @NotNull(message = "Số lượng CV tối đa không được để trống")
    @Min(value = 0, message = "Số lượng CV tối đa không được âm")
    @Max(value = 1000, message = "Số lượng CV tối đa không được vượt quá 1000")
    Integer maxCvs;

    @NotNull(message = "Số lượng đơn ứng tuyển tối đa không được để trống")
    @Min(value = 0, message = "Số lượng đơn ứng tuyển tối đa không được âm")
    @Max(value = 5000, message = "Số lượng đơn ứng tuyển tối đa không được vượt quá 5000")
    Integer maxJobApplications;

    Boolean featuredCv;

    Boolean aiJobMatching;

    Boolean supportPriority;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    Boolean isActive;
}
