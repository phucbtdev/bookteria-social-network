package com.recruitment.employer_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "employer_packages")
public class EmployerPackage extends BaseEntity {
    @Column(name = "name", length = 100, nullable = false, unique = true)
    String name;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "price", nullable = false)
    Integer price;

    @Column(name = "duration_days", nullable = false)
    Integer durationDays;

    @Column(name = "max_job_posts", nullable = false)
    Integer maxJobPosts;

    @Builder.Default
    @Column(name = "max_featured_jobs", nullable = false)
    Integer maxFeaturedJobs = 0;

    @Builder.Default
    @Column(name = "priority_support", nullable = false)
    Boolean prioritySupport = false;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;
}
