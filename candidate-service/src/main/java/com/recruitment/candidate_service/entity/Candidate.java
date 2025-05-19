package com.recruitment.candidate_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "candidates")
public class Candidate extends BaseEntity{

    UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    CandidatePackageSubscription subscription;

    LocalDate packageExpiryDate;

    String fullName;

    @Column(columnDefinition = "text")
    String avatarUrl;

    @Column(columnDefinition = "text")
    String resumeUrl;

    String linkedinUrl;

    String portfolioUrl;

}
