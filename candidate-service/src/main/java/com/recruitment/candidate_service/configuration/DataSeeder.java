package com.recruitment.candidate_service.configuration;

import com.recruitment.candidate_service.entity.CandidatePackage;
import com.recruitment.candidate_service.repository.CandidatePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final CandidatePackageRepository candidatePackageRepository;
    @Override
    public void run(String... args) throws Exception {
        if (candidatePackageRepository.count() == 0) {
            List<CandidatePackage> packages = List.of(
                    CandidatePackage.builder()
                            .name("Free")
                            .description("Free package")
                            .price(new BigDecimal("0.00"))
                            .durationDays(100)
                            .maxCvs(3)
                            .maxJobApplications(5)
                            .featuredCv(false)
                            .aiJobMatching(false)
                            .supportPriority(false)
                            .isActive(true)
                            .build(),
                    CandidatePackage.builder()
                            .name("Pro")
                            .description("Pro package")
                            .price(new BigDecimal("9.99"))
                            .durationDays(30)
                            .maxCvs(20)
                            .maxJobApplications(20)
                            .featuredCv(false)
                            .aiJobMatching(false)
                            .supportPriority(false)
                            .isActive(true)
                            .build(),
                    CandidatePackage.builder()
                            .name("Premium")
                            .description("Premium")
                            .price(new BigDecimal("19.99"))
                            .durationDays(60)
                            .maxCvs(50)
                            .maxJobApplications(100)
                            .featuredCv(true)
                            .aiJobMatching(true)
                            .supportPriority(true)
                            .isActive(true)
                            .build()
            );
            candidatePackageRepository.saveAll(packages);
        }
    }
}
