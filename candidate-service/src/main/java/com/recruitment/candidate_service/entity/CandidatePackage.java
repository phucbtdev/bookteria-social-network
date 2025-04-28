package com.recruitment.candidate_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "candidate_packages")
public class CandidatePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer  id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    String name;

    @Column(length = 500)
    String description;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @Column(name = "duration_days", nullable = false)
    Integer durationDays;

    @Column(name = "max_cvs")
    Integer maxCvs;

    @Column(name = "max_job_applications")
    Integer maxJobApplications;

    @Builder.Default
    @Column(name = "featured_cv")
    Boolean featuredCv = false;

    @Builder.Default
    @Column(name = "ai_job_matching")
    Boolean aiJobMatching = false;

    @Builder.Default
    @Column(name = "support_priority")
    Boolean supportPriority = false;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    Boolean isActive = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
