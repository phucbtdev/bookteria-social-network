package com.recruitment.candidate_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "candidates")
public class Candidate extends BaseEntity{

    UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    CandidatePackageSubscription subscriptionId;

    LocalDate packageExpiryDate;

    String fullName;

    @Column(columnDefinition = "text")
    String avatarUrl;

    @Column(columnDefinition = "text")
    String resumeUrl;

    String linkedinUrl;

    String portfolioUrl;

}
