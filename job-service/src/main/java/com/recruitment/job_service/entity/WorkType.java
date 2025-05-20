package com.recruitment.job_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "work_types")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkType extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "description")
    String description;

    @Builder.Default
    @OneToMany(mappedBy = "workType")
    Set<Job> jobs = new HashSet<>();
}

