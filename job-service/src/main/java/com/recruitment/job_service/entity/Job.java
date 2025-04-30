package com.recruitment.job_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "jobs")
public class Job extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    UUID id;

    @Column(name = "employer_id")
    UUID employerId;

    @Column(name = "title", length = 255)
    String title;

    @Column(name = "slug", length = 255, nullable = false, unique = true)
    String slug;

    @Column(name = "description", columnDefinition = "text")
    String description;

    @Column(name = "industry_id")
    UUID industryId;

    @Column(name = "job_level_id")
    UUID jobLevelId;

    @Column(name = "experience_level_id")
    UUID experienceLevelId;

    @Column(name = "salary_range_id")
    UUID salaryRangeId;

    @Column(name = "work_type_id")
    UUID workTypeId;

    @Column(name = "number_of_positions", columnDefinition = "INT DEFAULT 1")
    Integer numberOfPositions;

    @Column(name = "skills_required", columnDefinition = "text[]")
    @ElementCollection
    List<String> skillsRequired;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_requirement", columnDefinition = "varchar(6) DEFAULT 'ANY'")
    GenderRequirement genderRequirement;

    @Column(name = "location", columnDefinition = "text")
    String location;

    @Column(name = "address", columnDefinition = "text")
    String address;

    @Column(name = "latitude", precision = 10, scale = 8)
    BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    BigDecimal longitude;

    @Column(name = "application_deadline", nullable = false)
    LocalDate applicationDeadline;

    @Column(name = "start_date", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    JobPostStatus status;

    public enum GenderRequirement {
        ANY, MALE, FEMALE
    }

    public enum JobPostStatus {
        PENDING, APPROVED, REJECTED
    }
}
