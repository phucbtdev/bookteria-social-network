package com.recruitment.candidate_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

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
public class Candidate {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    UUID id;

    String userId;

    Integer  currentPackageId;

    LocalDate packageExpiryDate;

    @Column(length = 255)
    String fullName;

    @Column(columnDefinition = "text")
    String avatarUrl;

    @Column(columnDefinition = "text")
    String resumeUrl;

    @Column(length = 255)
    String linkedinUrl;

    @Column(length = 255)
    String portfolioUrl;

}
