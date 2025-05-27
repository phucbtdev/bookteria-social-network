package com.recruitment.job_service.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_applications")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "candidate_id", nullable = false)
    UUID candidateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    Job job;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    String coverLetter;

    @Column(name = "resume_url")
    String resumeUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    ApplicationStatus status = ApplicationStatus.PENDING;

    @CreationTimestamp
    @Column(name = "applied_at", nullable = false, updatable = false)
    LocalDateTime appliedAt;

    public enum ApplicationStatus {
        PENDING, INTERVIEW, REJECTED, HIRED
    }
}
