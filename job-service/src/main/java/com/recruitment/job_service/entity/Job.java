package com.recruitment.job_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "jobs")
public class Job extends BaseEntity {

    @Column(name = "employer_id")
    UUID employerId;

    @Column(name = "title", length = 255)
    String title;

    @Column(name = "slug", length = 255, nullable = false, unique = true)
    String slug;

    @Column(name = "description", columnDefinition = "text")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id")
    Industry industry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_level_id")
    JobLevel jobLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_level_id")
    ExperienceLevel experienceLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_range_id")
    SalaryRange salaryRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_type_id")
    WorkType workType;

    @Column(name = "number_of_positions", columnDefinition = "INT DEFAULT 1")
    Integer numberOfPositions;

    @Column(name = "skills_required", columnDefinition = "TEXT")
    String skillsRequired;

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
