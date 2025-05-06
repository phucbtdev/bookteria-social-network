package com.recruitment.employer_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "employers")
public class Employer extends BaseEntity {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    UUID id;

    @Column(name = "user_id")
    UUID userId;

    @Column(name = "current_package_id")
    Integer currentPackageId;

    @Column(name = "package_expiry_date")
    LocalDate packageExpiryDate;

    String fullName;

    String phone;

    @Column(name = "company_name", length = 255, nullable = false)
    String companyName;

    @Column(name = "company_city", columnDefinition = "TEXT")
    String companyCity;

    @Column(name = "company_website", length = 255)
    String companyWebsite;

    @Column(name = "company_logo_url", columnDefinition = "TEXT")
    String companyLogoUrl;

    @Column(name = "company_size", length = 50)
    String companySize;

    @Column(name = "industry", length = 255)
    String industry;

    @Column(name = "company_description", columnDefinition = "TEXT")
    String companyDescription;

    @Column(name = "is_verified")
    Boolean isVerified = false;

}
