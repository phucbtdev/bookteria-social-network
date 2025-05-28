package com.recruitment.employer_service.configuration;

import com.recruitment.employer_service.entity.Employer;
import com.recruitment.employer_service.entity.EmployerPackage;
import com.recruitment.employer_service.entity.EmployerPackageSubscriptions;
import com.recruitment.employer_service.repository.EmployerPackageRepository;
import com.recruitment.employer_service.repository.EmployerPackageSubscriptionRepository;
import com.recruitment.employer_service.repository.EmployerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final EmployerRepository employerRepository;
    private final EmployerPackageRepository packageRepository;
    private final EmployerPackageSubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (packageRepository.count() > 0) {
            return; // Đã có dữ liệu, không cần seed nữa
        }

        // Tạo 3 gói dịch vụ
        List<EmployerPackage> packages = createPackages();
        packageRepository.saveAll(packages);

        // Tạo 10 nhà tuyển dụng và đăng ký gói
        List<Employer> employers = new ArrayList<>();
        List<EmployerPackageSubscriptions> subscriptions = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            // Chọn ngẫu nhiên 1 trong 3 gói
            EmployerPackage selectedPackage = packages.get(i % 3);

            // Tạo subscription trước
            EmployerPackageSubscriptions subscription = EmployerPackageSubscriptions.builder()
                    .employerPackage(selectedPackage)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(selectedPackage.getDurationDays()))
                    .isActive(true)
                    .status("ACTIVE")
                    .build();

            // Tạo employer
            Employer employer = new Employer();
            employer.setUserId(UUID.randomUUID());
            employer.setFullName("Employer " + i);
            employer.setPhone("098765432" + i);
            employer.setCompanyName("Company " + i);
            employer.setCompanyCity(getRandomCity(i));
            employer.setCompanyWebsite("https://company" + i + ".com");
            employer.setCompanyLogoUrl("https://example.com/logos/company" + i + ".png");
            employer.setCompanySize(getRandomCompanySize(i));
            employer.setIndustry(getRandomIndustry(i));
            employer.setCompanyDescription("This is a description for Company " + i + ". They specialize in " +
                    getRandomIndustry(i) + " and have been operating for " + (i + 5) + " years.");
            employer.setIsVerified(i % 3 == 0); // Mỗi 3 công ty sẽ được xác minh
            employer.setPackageExpiryDate(LocalDate.now().plusDays(selectedPackage.getDurationDays()));

            employers.add(employer);

            // Lưu employer trước để có ID
            employerRepository.save(employer);

            // Thiết lập mối quan hệ hai chiều
            subscription.setEmployer(employer);
            subscriptions.add(subscription);

            // Lưu subscription
            subscriptionRepository.save(subscription);

            // Cập nhật lại employer với subscription
            employer.setSubscription(subscription);
            employerRepository.save(employer);
        }

    }

    private List<EmployerPackage> createPackages() {
        List<EmployerPackage> packages = new ArrayList<>();

        // Gói Basic
        EmployerPackage basicPackage = EmployerPackage.builder()
                .name("Basic")
                .description("Gói cơ bản dành cho nhà tuyển dụng nhỏ với nhu cầu tuyển dụng vừa phải.")
                .price(1000000) // 1 triệu VND
                .durationDays(30)
                .maxJobPosts(5)
                .maxFeaturedJobs(0)
                .prioritySupport(false)
                .isActive(true)
                .build();
        packages.add(basicPackage);

        // Gói Premium
        EmployerPackage premiumPackage = EmployerPackage.builder()
                .name("Premium")
                .description("Gói cao cấp dành cho doanh nghiệp vừa với nhiều vị trí tuyển dụng hơn.")
                .price(3000000) // 3 triệu VND
                .durationDays(60)
                .maxJobPosts(15)
                .maxFeaturedJobs(3)
                .prioritySupport(true)
                .isActive(true)
                .build();
        packages.add(premiumPackage);

        // Gói Enterprise
        EmployerPackage enterprisePackage = EmployerPackage.builder()
                .name("Enterprise")
                .description("Gói doanh nghiệp dành cho các công ty lớn với nhu cầu tuyển dụng cao.")
                .price(8000000) // 8 triệu VND
                .durationDays(90)
                .maxJobPosts(50)
                .maxFeaturedJobs(10)
                .prioritySupport(true)
                .isActive(true)
                .build();
        packages.add(enterprisePackage);

        return packages;
    }

    private String getRandomCity(int seed) {
        String[] cities = {"Hà Nội", "TP.HCM", "Đà Nẵng", "Hải Phòng", "Cần Thơ", "Huế", "Nha Trang"};
        return cities[seed % cities.length];
    }

    private String getRandomCompanySize(int seed) {
        String[] sizes = {"1-10", "11-50", "51-200", "201-500", "501-1000", "1000+"};
        return sizes[seed % sizes.length];
    }

    private String getRandomIndustry(int seed) {
        String[] industries = {
                "Công nghệ thông tin", "Tài chính - Ngân hàng", "Giáo dục - Đào tạo",
                "Bán lẻ", "Sản xuất", "Y tế - Dược phẩm", "Xây dựng", "Du lịch - Nhà hàng",
                "Logistics - Vận tải", "Truyền thông - Marketing"
        };
        return industries[seed % industries.length];
    }
}