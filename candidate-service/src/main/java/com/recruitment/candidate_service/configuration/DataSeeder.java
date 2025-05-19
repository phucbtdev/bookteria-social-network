package com.recruitment.candidate_service.configuration;

import com.recruitment.candidate_service.entity.CandidatePackage;
import com.recruitment.candidate_service.repository.CandidatePackageRepository;
import com.recruitment.candidate_service.entity.Candidate;
import com.recruitment.candidate_service.entity.CandidatePackageSubscription;
import com.recruitment.candidate_service.repository.CandidatePackageSubscriptionRepository;
import com.recruitment.candidate_service.repository.CandidateRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.time.LocalDate;
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

    public void run(String... args) throws Exception {
        if (packageRepository.count() > 0) {
            return;
        }

        seedCandidatePackages();
        seedCandidates();
        seedSubscriptions();
    }

    private void seedCandidatePackages() {
        // Create 10 different CandidatePackage entities
        CandidatePackage basicPackage = CandidatePackage.builder()
                .name("Basic")
                .description("Basic package for entry-level job seekers")
                .price(new BigDecimal("19.99"))
                .durationDays(30)
                .maxCvs(1)
                .maxJobApplications(10)
                .featuredCv(false)
                .aiJobMatching(false)
                .supportPriority(false)
                .isActive(true)
                .build();

        CandidatePackage standardPackage = CandidatePackage.builder()
                .name("Standard")
                .description("Standard package with enhanced features")
                .price(new BigDecimal("49.99"))
                .durationDays(60)
                .maxCvs(2)
                .maxJobApplications(25)
                .featuredCv(true)
                .aiJobMatching(false)
                .supportPriority(false)
                .isActive(true)
                .build();

        CandidatePackage premiumPackage = CandidatePackage.builder()
                .name("Premium")
                .description("Premium package for professionals with all features")
                .price(new BigDecimal("99.99"))
                .durationDays(90)
                .maxCvs(5)
                .maxJobApplications(50)
                .featuredCv(true)
                .aiJobMatching(true)
                .supportPriority(true)
                .isActive(true)
                .build();

        CandidatePackage executivePackage = CandidatePackage.builder()
                .name("Executive")
                .description("Executive package for senior professionals")
                .price(new BigDecimal("199.99"))
                .durationDays(180)
                .maxCvs(10)
                .maxJobApplications(100)
                .featuredCv(true)
                .aiJobMatching(true)
                .supportPriority(true)
                .isActive(true)
                .build();

        CandidatePackage freelancerPackage = CandidatePackage.builder()
                .name("Freelancer")
                .description("Special package for freelancers and contractors")
                .price(new BigDecimal("79.99"))
                .durationDays(60)
                .maxCvs(3)
                .maxJobApplications(30)
                .featuredCv(true)
                .aiJobMatching(true)
                .supportPriority(false)
                .isActive(true)
                .build();

        CandidatePackage techCareerPackage = CandidatePackage.builder()
                .name("Tech Career")
                .description("Specialized package for tech professionals")
                .price(new BigDecimal("129.99"))
                .durationDays(90)
                .maxCvs(3)
                .maxJobApplications(40)
                .featuredCv(true)
                .aiJobMatching(true)
                .supportPriority(true)
                .isActive(true)
                .build();

        CandidatePackage annualPackage = CandidatePackage.builder()
                .name("Annual Pro")
                .description("Annual subscription with all premium features")
                .price(new BigDecimal("299.99"))
                .durationDays(365)
                .maxCvs(15)
                .maxJobApplications(200)
                .featuredCv(true)
                .aiJobMatching(true)
                .supportPriority(true)
                .isActive(true)
                .build();

        CandidatePackage startupPackage = CandidatePackage.builder()
                .name("Startup Special")
                .description("Affordable package for startup employees")
                .price(new BigDecimal("39.99"))
                .durationDays(45)
                .maxCvs(2)
                .maxJobApplications(20)
                .featuredCv(false)
                .aiJobMatching(true)
                .supportPriority(false)
                .isActive(true)
                .build();

        CandidatePackage academicPackage = CandidatePackage.builder()
                .name("Academic")
                .description("Special package for students and researchers")
                .price(new BigDecimal("29.99"))
                .durationDays(120)
                .maxCvs(2)
                .maxJobApplications(15)
                .featuredCv(false)
                .aiJobMatching(true)
                .supportPriority(false)
                .isActive(true)
                .build();

        CandidatePackage inactivePackage = CandidatePackage.builder()
                .name("Legacy Premium")
                .description("Previous version of premium subscription (no longer offered)")
                .price(new BigDecimal("89.99"))
                .durationDays(90)
                .maxCvs(3)
                .maxJobApplications(30)
                .featuredCv(true)
                .aiJobMatching(false)
                .supportPriority(true)
                .isActive(false)
                .build();

        packageRepository.saveAll(Arrays.asList(
                basicPackage,
                standardPackage,
                premiumPackage,
                executivePackage,
                freelancerPackage,
                techCareerPackage,
                annualPackage,
                startupPackage,
                academicPackage,
                inactivePackage
        ));
    }

    private void seedCandidates() {
        // Create 10 different Candidate entities
        Candidate candidate1 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("John Smith")
                .avatarUrl("https://example.com/avatars/john.jpg")
                .resumeUrl("https://example.com/resumes/john_smith_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/johnsmith")
                .portfolioUrl("https://johnsmith-portfolio.com")
                .build();

        Candidate candidate2 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("Sarah Johnson")
                .avatarUrl("https://example.com/avatars/sarah.jpg")
                .resumeUrl("https://example.com/resumes/sarah_johnson_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/sarahjohnson")
                .portfolioUrl("https://sarahjohnson-portfolio.io")
                .build();

        Candidate candidate3 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("Michael Chen")
                .avatarUrl("https://example.com/avatars/michael.jpg")
                .resumeUrl("https://example.com/resumes/michael_chen_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/michaelchen")
                .portfolioUrl(null)
                .build();

        Candidate candidate4 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("Emma Williams")
                .avatarUrl("https://example.com/avatars/emma.jpg")
                .resumeUrl("https://example.com/resumes/emma_williams_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/emmawilliams")
                .portfolioUrl("https://emmawilliams.design")
                .build();

        Candidate candidate5 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("David Rodriguez")
                .avatarUrl("https://example.com/avatars/david.jpg")
                .resumeUrl("https://example.com/resumes/david_rodriguez_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/davidrodriguez")
                .portfolioUrl("https://davidrodriguez.dev")
                .build();

        Candidate candidate6 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("Olivia Kim")
                .avatarUrl("https://example.com/avatars/olivia.jpg")
                .resumeUrl("https://example.com/resumes/olivia_kim_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/oliviakim")
                .portfolioUrl(null)
                .build();

        Candidate candidate7 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("James Wilson")
                .avatarUrl("https://example.com/avatars/james.jpg")
                .resumeUrl("https://example.com/resumes/james_wilson_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/jameswilson")
                .portfolioUrl("https://jameswilson.tech")
                .build();

        Candidate candidate8 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("Sophia Lee")
                .avatarUrl("https://example.com/avatars/sophia.jpg")
                .resumeUrl("https://example.com/resumes/sophia_lee_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/sophialee")
                .portfolioUrl("https://sophialee-portfolio.com")
                .build();

        Candidate candidate9 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("Daniel Martinez")
                .avatarUrl("https://example.com/avatars/daniel.jpg")
                .resumeUrl("https://example.com/resumes/daniel_martinez_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/danielmartinez")
                .portfolioUrl(null)
                .build();

        Candidate candidate10 = Candidate.builder()
                .userId(UUID.randomUUID())
                .fullName("Mia Patel")
                .avatarUrl("https://example.com/avatars/mia.jpg")
                .resumeUrl("https://example.com/resumes/mia_patel_resume.pdf")
                .linkedinUrl("https://linkedin.com/in/miapatel")
                .portfolioUrl("https://miapatel.co")
                .build();

        candidateRepository.saveAll(Arrays.asList(
                candidate1,
                candidate2,
                candidate3,
                candidate4,
                candidate5,
                candidate6,
                candidate7,
                candidate8,
                candidate9,
                candidate10
        ));
    }

    private void seedSubscriptions() {
        // Get all candidates and packages
        var candidates = candidateRepository.findAll();
        var packages = packageRepository.findAll();

        // Create 10 different CandidatePackageSubscription entities with different statuses
        CandidatePackageSubscription subscription1 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(0))
                .candidatePackage(packages.get(0))
                .startDate(LocalDate.now().minusDays(15))
                .endDate(LocalDate.now().plusDays(15))
                .amountPaid(packages.get(0).getPrice())
                .jobApplicationsUsed(3)
                .paymentReference("REF-123456-A")
                .status(CandidatePackageSubscription.SubscriptionStatus.ACTIVE)
                .build();

        CandidatePackageSubscription subscription2 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(1))
                .candidatePackage(packages.get(1))
                .startDate(LocalDate.now().minusDays(30))
                .endDate(LocalDate.now().plusDays(30))
                .amountPaid(packages.get(1).getPrice())
                .jobApplicationsUsed(10)
                .paymentReference("REF-234567-B")
                .status(CandidatePackageSubscription.SubscriptionStatus.ACTIVE)
                .build();

        CandidatePackageSubscription subscription3 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(2))
                .candidatePackage(packages.get(2))
                .startDate(LocalDate.now().minusDays(100))
                .endDate(LocalDate.now().minusDays(10))
                .amountPaid(packages.get(2).getPrice())
                .jobApplicationsUsed(25)
                .paymentReference("REF-345678-C")
                .status(CandidatePackageSubscription.SubscriptionStatus.EXPIRED)
                .build();

        CandidatePackageSubscription subscription4 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(3))
                .candidatePackage(packages.get(3))
                .startDate(LocalDate.now().minusDays(20))
                .endDate(LocalDate.now().plusDays(160))
                .amountPaid(packages.get(3).getPrice())
                .jobApplicationsUsed(15)
                .paymentReference("REF-456789-D")
                .status(CandidatePackageSubscription.SubscriptionStatus.ACTIVE)
                .build();

        CandidatePackageSubscription subscription5 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(4))
                .candidatePackage(packages.get(4))
                .startDate(LocalDate.now().minusDays(30))
                .endDate(LocalDate.now().plusDays(30))
                .amountPaid(packages.get(4).getPrice())
                .jobApplicationsUsed(12)
                .paymentReference("REF-567890-E")
                .status(CandidatePackageSubscription.SubscriptionStatus.CANCELLED)
                .cancelledAt(LocalDate.now().minusDays(5).atStartOfDay())
                .build();

        CandidatePackageSubscription subscription6 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(5))
                .candidatePackage(packages.get(5))
                .startDate(LocalDate.now().plusDays(5))
                .endDate(LocalDate.now().plusDays(95))
                .amountPaid(packages.get(5).getPrice())
                .jobApplicationsUsed(0)
                .paymentReference("REF-678901-F")
                .status(CandidatePackageSubscription.SubscriptionStatus.PENDING)
                .build();

        CandidatePackageSubscription subscription7 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(6))
                .candidatePackage(packages.get(6))
                .startDate(LocalDate.now().minusDays(30))
                .endDate(LocalDate.now().plusDays(335))
                .amountPaid(packages.get(6).getPrice())
                .jobApplicationsUsed(8)
                .paymentReference("REF-789012-G")
                .status(CandidatePackageSubscription.SubscriptionStatus.ACTIVE)
                .build();

        CandidatePackageSubscription subscription8 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(7))
                .candidatePackage(packages.get(7))
                .startDate(LocalDate.now().minusDays(10))
                .endDate(LocalDate.now().plusDays(35))
                .amountPaid(packages.get(7).getPrice())
                .jobApplicationsUsed(5)
                .paymentReference("REF-890123-H")
                .status(CandidatePackageSubscription.SubscriptionStatus.ACTIVE)
                .build();

        CandidatePackageSubscription subscription9 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(8))
                .candidatePackage(packages.get(8))
                .startDate(LocalDate.now().minusDays(130))
                .endDate(LocalDate.now().minusDays(10))
                .amountPaid(packages.get(8).getPrice())
                .jobApplicationsUsed(15)
                .paymentReference("REF-901234-I")
                .status(CandidatePackageSubscription.SubscriptionStatus.EXPIRED)
                .build();

        CandidatePackageSubscription subscription10 = CandidatePackageSubscription.builder()
                .candidate(candidates.get(9))
                .candidatePackage(packages.get(9))
                .startDate(LocalDate.now().minusDays(50))
                .endDate(LocalDate.now().plusDays(40))
                .amountPaid(packages.get(9).getPrice())
                .jobApplicationsUsed(22)
                .paymentReference("REF-012345-J")
                .status(CandidatePackageSubscription.SubscriptionStatus.ACTIVE)
                .build();

        subscriptionRepository.saveAll(Arrays.asList(
                subscription1,
                subscription2,
                subscription3,
                subscription4,
                subscription5,
                subscription6,
                subscription7,
                subscription8,
                subscription9,
                subscription10
        ));

        // Update candidates with their subscription
        candidates.get(0).setSubscription(subscription1);
        candidates.get(0).setPackageExpiryDate(subscription1.getEndDate());

        candidates.get(1).setSubscription(subscription2);
        candidates.get(1).setPackageExpiryDate(subscription2.getEndDate());

        candidates.get(2).setSubscription(subscription3);
        candidates.get(2).setPackageExpiryDate(subscription3.getEndDate());

        candidates.get(3).setSubscription(subscription4);
        candidates.get(3).setPackageExpiryDate(subscription4.getEndDate());

        candidates.get(4).setSubscription(subscription5);
        candidates.get(4).setPackageExpiryDate(subscription5.getEndDate());

        candidates.get(5).setSubscription(subscription6);
        candidates.get(5).setPackageExpiryDate(subscription6.getEndDate());

        candidates.get(6).setSubscription(subscription7);
        candidates.get(6).setPackageExpiryDate(subscription7.getEndDate());

        candidates.get(7).setSubscription(subscription8);
        candidates.get(7).setPackageExpiryDate(subscription8.getEndDate());

        candidates.get(8).setSubscription(subscription9);
        candidates.get(8).setPackageExpiryDate(subscription9.getEndDate());

        candidates.get(9).setSubscription(subscription10);
        candidates.get(9).setPackageExpiryDate(subscription10.getEndDate());

        candidateRepository.saveAll(candidates);
    }
}
