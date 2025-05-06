package com.recruitment.employer_service.configuration;

import com.recruitment.employer_service.entity.EmployerPackage;
import com.recruitment.employer_service.repository.EmployerPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final EmployerPackageRepository employerPackageRepository;
    @Override
    public void run(String... args) throws Exception {
        if (employerPackageRepository.count() == 0) {
            List<EmployerPackage> packages = List.of(
                    EmployerPackage.builder()
                            .name("Gói Cơ Bản")
                            .description("Phù hợp với nhà tuyển dụng mới bắt đầu.")
                            .price(500000)
                            .durationDays(30)
                            .maxJobPosts(3)
                            .maxFeaturedJobs(0)
                            .prioritySupport(false)
                            .isActive(true)
                            .build(),
                    EmployerPackage.builder()
                            .name("Gói Cao Cấp")
                            .description("Dành cho nhà tuyển dụng cần nhiều tính năng nâng cao.")
                            .price(2000000)
                            .durationDays(60)
                            .maxJobPosts(15)
                            .maxFeaturedJobs(5)
                            .prioritySupport(true)
                            .isActive(true)
                            .build(),
                    EmployerPackage.builder()
                            .name("Gói Doanh Nghiệp")
                            .description("Gói đặc biệt cho doanh nghiệp lớn có nhu cầu tuyển dụng cao.")
                            .price(5000000)
                            .durationDays(90)
                            .maxJobPosts(50)
                            .maxFeaturedJobs(20)
                            .prioritySupport(true)
                            .isActive(true)
                            .build()
            );
            employerPackageRepository.saveAll(packages);
        }
    }
}
