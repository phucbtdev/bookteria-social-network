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

    Integer  currentPackageId;

    LocalDate packageExpiryDate;

    String fullName;

    @Column(columnDefinition = "text")
    String avatarUrl;

    @Column(columnDefinition = "text")
    String resumeUrl;

    String linkedinUrl;

    String portfolioUrl;

}
