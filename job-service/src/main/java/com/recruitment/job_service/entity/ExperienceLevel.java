package com.recruitment.job_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "experience_levels")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExperienceLevel extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "min_years")
    Integer minYears;

    @Column(name = "max_years")
    Integer maxYears;

    @Column(name = "description")
    String description;

    // Ví dụ: No experience, <1 year, 1-3 years, 3-5 years, 5-10 years, 10+ years
    @Builder.Default
    @OneToMany(mappedBy = "experienceLevel")
    Set<Job> jobs = new HashSet<>();
}