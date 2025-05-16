package com.recruitment.candidate_service.configuration;

import com.recruitment.candidate_service.entity.CandidatePackage;
import com.recruitment.candidate_service.repository.CandidatePackageRepository;
import com.recruitment.candidate_service.entity.Candidate;
import com.recruitment.candidate_service.entity.CandidatePackageSubscription;
import com.recruitment.candidate_service.entity.CandidatePackageSubscription.SubscriptionStatus;
import com.recruitment.candidate_service.repository.CandidatePackageSubscriptionRepository;
import com.recruitment.candidate_service.repository.CandidateRepository;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CandidateRepository candidateRepository;
    private final CandidatePackageRepository packageRepository;
    private final CandidatePackageSubscriptionRepository subscriptionRepository;

    @Override
    public void run(String... args) {
        // Seed candidate packages
        List<CandidatePackage> packages = seedCandidatePackages();

        // Seed candidates
        List<Candidate> candidates = seedCandidates(packages);

        // Seed subscriptions
        seedCandidatePackageSubscriptions(candidates, packages);
    }

    private List<CandidatePackage> seedCandidatePackages() {
        List<CandidatePackage> packages = new ArrayList<>();

        // Only seed if no packages exist
        if (packageRepository.count() == 0) {
            // Package 1: Free
            packages.add(CandidatePackage.builder()
                    .name("Free")
                    .description("Basic package for new candidates")
                    .price(BigDecimal.ZERO)
                    .durationDays(30)
                    .maxCvs(1)
                    .maxJobApplications(5)
                    .featuredCv(false)
                    .aiJobMatching(false)
                    .supportPriority(false)
                    .isActive(true)
                    .build());

            // Package 2: Basic
            packages.add(CandidatePackage.builder()
                    .name("Basic")
                    .description("Standard package with improved features")
                    .price(new BigDecimal("29.99"))
                    .durationDays(30)
                    .maxCvs(2)
                    .maxJobApplications(10)
                    .featuredCv(false)
                    .aiJobMatching(true)
                    .supportPriority(false)
                    .isActive(true)
                    .build());

            // Package 3: Premium
            packages.add(CandidatePackage.builder()
                    .name("Premium")
                    .description("Premium package with all features")
                    .price(new BigDecimal("59.99"))
                    .durationDays(30)
                    .maxCvs(5)
                    .maxJobApplications(20)
                    .featuredCv(true)
                    .aiJobMatching(true)
                    .supportPriority(true)
                    .isActive(true)
                    .build());

            // Package 4: Annual
            packages.add(CandidatePackage.builder()
                    .name("Annual")
                    .description("Annual premium package with best value")
                    .price(new BigDecimal("499.99"))
                    .durationDays(365)
                    .maxCvs(10)
                    .maxJobApplications(100)
                    .featuredCv(true)
                    .aiJobMatching(true)
                    .supportPriority(true)
                    .isActive(true)
                    .build());

            // Package 5: Student
            packages.add(CandidatePackage.builder()
                    .name("Student")
                    .description("Special package for students")
                    .price(new BigDecimal("19.99"))
                    .durationDays(60)
                    .maxCvs(3)
                    .maxJobApplications(15)
                    .featuredCv(false)
                    .aiJobMatching(true)
                    .supportPriority(false)
                    .isActive(true)
                    .build());

            // Package 6: Inactive package
            packages.add(CandidatePackage.builder()
                    .name("Deprecated Package")
                    .description("Old package no longer available")
                    .price(new BigDecimal("39.99"))
                    .durationDays(30)
                    .maxCvs(3)
                    .maxJobApplications(15)
                    .featuredCv(true)
                    .aiJobMatching(false)
                    .supportPriority(false)
                    .isActive(false)
                    .build());

            packages = packageRepository.saveAll(packages);
        } else {
            packages = packageRepository.findAll();
        }

        return packages;
    }

    private List<Candidate> seedCandidates(List<CandidatePackage> packages) {
        List<Candidate> candidates = new ArrayList<>();

        // Only seed if no candidates exist
        if (candidateRepository.count() == 0) {
            // 1. Create 20 candidates
            for (int i = 1; i <= 20; i++) {
                CandidatePackage randomPackage = packages.get(i % packages.size());

                candidates.add(Candidate.builder()
                        .id(UUID.randomUUID())
                        .userId(UUID.randomUUID())
                        .currentPackageId(randomPackage.getId())
                        .packageExpiryDate(LocalDate.now().plusDays(randomPackage.getDurationDays() / 2)) // Halfway through
                        .fullName("Candidate " + i)
                        .avatarUrl("https://example.com/avatars/candidate" + i + ".jpg")
                        .resumeUrl("https://example.com/resumes/candidate" + i + ".pdf")
                        .linkedinUrl("https://linkedin.com/in/candidate" + i)
                        .portfolioUrl(i % 3 == 0 ? "https://portfolio.candidate" + i + ".com" : null)
                        .build());
            }

            candidates = candidateRepository.saveAll(candidates);
        } else {
            candidates = candidateRepository.findAll();
        }

        return candidates;
    }

    private void seedCandidatePackageSubscriptions(List<Candidate> candidates, List<CandidatePackage> packages) {
        // Only seed if no subscriptions exist
        if (subscriptionRepository.count() == 0) {
            List<CandidatePackageSubscription> subscriptions = new ArrayList<>();
            LocalDate now = LocalDate.now();

            // Create at least 20 subscriptions
            for (int i = 0; i < 20; i++) {
                Candidate candidate = candidates.get(i % candidates.size());
                CandidatePackage candidatePackage = packages.get(i % packages.size());

                // Calculate some varied data
                LocalDate startDate = now.minusDays(i * 5 % 60);
                LocalDate endDate = startDate.plusDays(candidatePackage.getDurationDays());
                int jobApplicationsUsed = i % (candidatePackage.getMaxJobApplications() + 1);

                // Determine status based on dates and a variety pattern
                SubscriptionStatus status;
                LocalDateTime cancelledAt = null;

                if (i % 8 == 0) {
                    status = SubscriptionStatus.CANCELLED;
                    cancelledAt = LocalDateTime.now().minusDays(i % 10);
                } else if (endDate.isBefore(now)) {
                    status = SubscriptionStatus.EXPIRED;
                } else if (i % 7 == 0) {
                    status = SubscriptionStatus.PENDING;
                } else {
                    status = SubscriptionStatus.ACTIVE;
                }

                // Create subscription
                subscriptions.add(CandidatePackageSubscription.builder()
                        .id(UUID.randomUUID())
                        .candidate(candidate)
                        .candidatePackage(candidatePackage)
                        .startDate(startDate)
                        .endDate(endDate)
                        .amountPaid(candidatePackage.getPrice())
                        .jobApplicationsUsed(jobApplicationsUsed)
                        .paymentReference("PAY-" + UUID.randomUUID().toString().substring(0, 8))
                        .status(status)
                        .cancelledAt(cancelledAt)
                        .build());
            }

            // Save all subscriptions
            subscriptionRepository.saveAll(subscriptions);
        }
    }
}