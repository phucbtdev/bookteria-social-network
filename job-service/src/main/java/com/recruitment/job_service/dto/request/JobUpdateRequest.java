package com.recruitment.job_service.dto.request;
import com.recruitment.job_service.entity.Job;
import com.recruitment.job_service.validation.UniqueJobSlug;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobUpdateRequest {
    @NotBlank(message = "Slug là bắt buộc")
    @Size(max = 255, message = "Slug không quá 255 ký tự")
    @UniqueJobSlug
    String slug;

    @NotBlank(message = "Tên công việc là bắt buộc")
    @Size(max = 255, message = "Tiêu đề không quá 255 ký tự")
    String title;

    @NotBlank(message = "Mô tả công việc là bắt buộc")
    String description;

    @NotNull(message = "Ngành nghề là bắt buộc")
    UUID industryId;

    @NotNull(message = "Cấp bậc công việc là bắt buộc")
    UUID jobLevelId;

    @NotNull(message = "Cấp độ kinh nghiệm là bắt buộc")
    UUID experienceLevelId;

    @NotNull(message = "Mức lương là bắt buộc")
    UUID salaryRangeId;

    @NotNull(message = "Hình thức làm việc là bắt buộc")
    UUID workTypeId;

    @Min(value = 1, message = "Number of positions must be at least 1")
    @Builder.Default
    Integer numberOfPositions = 1;

    String skillsRequired;

    @Builder.Default
    Job.GenderRequirement genderRequirement = Job.GenderRequirement.ANY;

    @NotBlank(message = "Địa chỉ công việc là bắt buộc")
    String address;

    @DecimalMin(value = "-90.0", message = "Latitude must be greater than or equal to -90")
    @DecimalMax(value = "90.0", message = "Latitude must be less than or equal to 90")
    BigDecimal latitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be greater than or equal to -180")
    @DecimalMax(value = "180.0", message = "Longitude must be less than or equal to 180")
    BigDecimal longitude;

    @NotNull(message = "Hạn nộp hồ sơ là bắt buộc")
    @Future(message = "Hạn nộp hồ sơ phải là một ngày trong tương")
    LocalDate applicationDeadline;

    @Builder.Default
    Job.JobPostStatus status = Job.JobPostStatus.PENDING;
}
