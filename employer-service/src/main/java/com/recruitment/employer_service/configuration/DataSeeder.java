package com.recruitment.employer_service.configuration;

import com.recruitment.employer_service.entity.Employer;
import com.recruitment.employer_service.entity.EmployerPackage;
import com.recruitment.employer_service.entity.EmployerPackageSubscriptions;
import com.recruitment.employer_service.repository.EmployerPackageRepository;
import com.recruitment.employer_service.repository.EmployerPackageSubscriptionRepository;
import com.recruitment.employer_service.repository.EmployerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final EmployerRepository employerRepository;
    private final EmployerPackageRepository employerPackageRepository;
    private final EmployerPackageSubscriptionRepository subscriptionsRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (employerPackageRepository.count() == 0) {
            seedEmployerPackages();
        }

        if (employerRepository.count() == 0) {
            seedEmployers();
        }

        if (subscriptionsRepository.count() == 0) {
            seedSubscriptions();
        }
    }

    private void seedEmployerPackages() {
        List<EmployerPackage> packages = new ArrayList<>();

        // Basic Package
        packages.add(EmployerPackage.builder()
                .name("Basic")
                .description("Entry-level package for small businesses. Post up to 5 jobs and get started with recruitment.")
                .price(99)
                .durationDays(30)
                .maxJobPosts(5)
                .maxFeaturedJobs(1)
                .prioritySupport(false)
                .isActive(true)
                .build());

        // Professional Package
        packages.add(EmployerPackage.builder()
                .name("Professional")
                .description("Mid-tier package for growing companies. Post up to 15 jobs with 5 featured listings and priority support.")
                .price(299)
                .durationDays(30)
                .maxJobPosts(15)
                .maxFeaturedJobs(5)
                .prioritySupport(true)
                .isActive(true)
                .build());

        // Enterprise Package
        packages.add(EmployerPackage.builder()
                .name("Enterprise")
                .description("Premium package for large organizations. Unlimited job posts, 10 featured listings, and priority customer support.")
                .price(599)
                .durationDays(30)
                .maxJobPosts(50)
                .maxFeaturedJobs(10)
                .prioritySupport(true)
                .isActive(true)
                .build());

        employerPackageRepository.saveAll(packages);
    }

    private void seedEmployers() {
        List<Employer> employers = new ArrayList<>();

        // List of company names
        String[] companyNames = {
                "TechSolutions Inc", "Global Innovations", "NextGen Systems",
                "Digital Enterprises", "Future Technologies", "Smart Solutions",
                "Innovative Systems", "Creative Technologies", "Premier Solutions",
                "Advanced Digital", "Tech Pioneers", "Global Tech Partners",
                "Modern Solutions", "Future Enterprises", "Elite Technologies",
                "Prime Innovations", "Strategic Systems", "Vision Tech",
                "Apex Solutions", "Horizon Technologies"
        };

        // List of cities
        String[] cities = {
                "Ho Chi Minh City", "Hanoi", "Da Nang", "Can Tho", "Hai Phong",
                "Nha Trang", "Vung Tau", "Da Lat", "Hue", "Quy Nhon"
        };

        // List of industries
        String[] industries = {
                "Information Technology", "Finance", "Healthcare", "Education",
                "Manufacturing", "Retail", "Telecommunications", "E-commerce",
                "Construction", "Hospitality"
        };

        // List of company sizes
        String[] companySizes = {
                "1-10", "11-50", "51-200", "201-500", "501-1000", "1000+"
        };

        // Create 20 employers
        for (int i = 0; i < 20; i++) {
            // Randomly select a package ID between 1 and 3
            int packageId = random.nextInt(3) + 1;

            // Generate random dates for package expiry (between 1 and 60 days from now)
            LocalDate expiryDate = LocalDate.now().plusDays(random.nextInt(60) + 1);

            employers.add(new Employer(
                    UUID.randomUUID(),
                    UUID.randomUUID(), // random user ID
                    packageId,
                    expiryDate,
                    "Employer " + (i + 1),
                    "0" + (90000000 + random.nextInt(10000000)), // random phone number
                    companyNames[i],
                    cities[random.nextInt(cities.length)],
                    "https://www." + companyNames[i].toLowerCase().replace(" ", "") + ".com",
                    "https://storage.cloudinary.com/company-logos/" + companyNames[i].toLowerCase().replace(" ", "") + ".png",
                    companySizes[random.nextInt(companySizes.length)],
                    industries[random.nextInt(industries.length)],
                    "A leading company in " + industries[random.nextInt(industries.length)] + " providing innovative solutions.",
                    random.nextBoolean()
            ));
        }

        employerRepository.saveAll(employers);
    }

    private void seedSubscriptions() {
        List<EmployerPackageSubscriptions> subscriptions = new ArrayList<>();

        // Get all employers
        List<Employer> employers = employerRepository.findAll();
        // Get all packages
        List<EmployerPackage> packages = employerPackageRepository.findAll();

        // Create subscription records for each employer
        for (Employer employer : employers) {
            // Get the current package for this employer
            EmployerPackage currentPackage = packages.stream()
                    .filter(pkg -> pkg.getId().equals(employer.getCurrentPackageId()))
                    .findFirst()
                    .orElse(packages.get(0)); // Default to first package if not found

            // Current active subscription
            LocalDate startDate = LocalDate.now().minusDays(random.nextInt(15));
            LocalDate endDate = employer.getPackageExpiryDate();

            subscriptions.add(EmployerPackageSubscriptions.builder()
                    .id(UUID.randomUUID())
                    .employer(employer)
                    .employerPackage(currentPackage)
                    .startDate(startDate)
                    .endDate(endDate)
                    .isActive(true)
                    .status("ACTIVE")
                    .build());

            // Maybe add a historical subscription (50% chance)
            if (random.nextBoolean()) {
                // Previous package (different from current)
                int prevPackageIndex = random.nextInt(packages.size());
                if (packages.get(prevPackageIndex).getId().equals(currentPackage.getId())) {
                    prevPackageIndex = (prevPackageIndex + 1) % packages.size();
                }

                LocalDate prevStartDate = startDate.minusDays(30);
                LocalDate prevEndDate = startDate.minusDays(1);

                subscriptions.add(EmployerPackageSubscriptions.builder()
                        .id(UUID.randomUUID())
                        .employer(employer)
                        .employerPackage(packages.get(prevPackageIndex))
                        .startDate(prevStartDate)
                        .endDate(prevEndDate)
                        .isActive(false)
                        .status("EXPIRED")
                        .build());
            }
        }

        subscriptionsRepository.saveAll(subscriptions);
    }
}