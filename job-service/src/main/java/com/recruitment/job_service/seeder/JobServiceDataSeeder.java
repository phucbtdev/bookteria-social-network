package com.recruitment.job_service.seeder;

import com.recruitment.job_service.entity.*;
import com.recruitment.job_service.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class JobServiceDataSeeder implements CommandLineRunner {

    @Autowired
    private ExperienceLevelRepository experienceLevelRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private JobLevelRepository jobLevelRepository;

    @Autowired
    private SalaryRangeRepository salaryRangeRepository;

    @Autowired
    private WorkTypeRepository workTypeRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        seedExperienceLevels();
        seedIndustries();
        seedJobLevels();
        seedSalaryRanges();
        seedWorkTypes();
    }

    private void seedExperienceLevels() {
        if (experienceLevelRepository.count() > 0) {
            return;
        }

        List<ExperienceLevel> experienceLevels = Arrays.asList(
                createExperienceLevel(
                        UUID.fromString("11111111-1111-1111-1111-111111111111"),
                        "No Experience", 0, 0, "No prior experience required"
                ),
                createExperienceLevel(
                        UUID.fromString("22222222-2222-2222-2222-222222222222"),
                        "Entry Level", 0, 1, "Less than 1 year of experience"
                ),
                createExperienceLevel(
                        UUID.fromString("33333333-3333-3333-3333-333333333333"),
                        "Junior", 1, 3, "1-3 years of experience"
                ),
                createExperienceLevel(
                        UUID.fromString("44444444-4444-4444-4444-444444444444"),
                        "Mid-Level", 3, 5, "3-5 years of experience"
                ),
                createExperienceLevel(
                        UUID.fromString("55555555-5555-5555-5555-555555555555"),
                        "Senior", 5, 10, "5-10 years of experience"
                ),
                createExperienceLevel(
                        UUID.fromString("66666666-6666-6666-6666-666666666666"),
                        "Expert", 10, null, "More than 10 years of experience"
                )
        );

        experienceLevelRepository.saveAll(experienceLevels);
    }

    private ExperienceLevel createExperienceLevel(UUID id, String name, Integer minYears, Integer maxYears, String description) {
        ExperienceLevel experienceLevel = new ExperienceLevel();
        experienceLevel.setId(id);
        experienceLevel.setName(name);
        experienceLevel.setMinYears(minYears);
        experienceLevel.setMaxYears(maxYears);
        experienceLevel.setDescription(description);
        experienceLevel.setCreatedAt(LocalDateTime.now());
        experienceLevel.setUpdatedAt(LocalDateTime.now());
        return experienceLevel;
    }

    private void seedIndustries() {
        if (industryRepository.count() > 0) {
            return;
        }

        List<Industry> industries = Arrays.asList(
                createIndustry(
                        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                        "Information Technology", "The IT sector including software development, hardware, and services"
                ),
                createIndustry(
                        UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"),
                        "Finance", "Banking, investment, and financial services"
                ),
                createIndustry(
                        UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"),
                        "Healthcare", "Medical services, pharmaceuticals, and healthcare technology"
                ),
                createIndustry(
                        UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"),
                        "Education", "Schools, universities, and educational services"
                ),
                createIndustry(
                        UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"),
                        "Manufacturing", "Production of goods and industrial processes"
                ),
                createIndustry(
                        UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff"),
                        "Retail", "Consumer goods and retail services"
                ),
                createIndustry(
                        UUID.fromString("11111111-aaaa-bbbb-cccc-dddddddddddd"),
                        "Marketing", "Advertising, public relations, and marketing services"
                ),
                createIndustry(
                        UUID.fromString("22222222-aaaa-bbbb-cccc-dddddddddddd"),
                        "Hospitality", "Hotels, restaurants, and tourism"
                )
        );

        industryRepository.saveAll(industries);
    }

    private Industry createIndustry(UUID id, String name, String description) {
        Industry industry = new Industry();
        industry.setId(id);
        industry.setName(name);
        industry.setDescription(description);
        industry.setCreatedAt(LocalDateTime.now());
        industry.setUpdatedAt(LocalDateTime.now());
        return industry;
    }

    private void seedJobLevels() {
        if (jobLevelRepository.count() > 0) {
            return;
        }

        List<JobLevel> jobLevels = Arrays.asList(
                createJobLevel(
                        UUID.fromString("77777777-7777-7777-7777-777777777777"),
                        "Intern", "Temporary position for students or recent graduates"
                ),
                createJobLevel(
                        UUID.fromString("88888888-8888-8888-8888-888888888888"),
                        "Entry Level", "Position for beginners in the field"
                ),
                createJobLevel(
                        UUID.fromString("99999999-9999-9999-9999-999999999999"),
                        "Junior", "Early career position with some experience"
                ),
                createJobLevel(
                        UUID.fromString("aaaaaaaa-1111-2222-3333-444444444444"),
                        "Mid-Level", "Position requiring moderate experience"
                ),
                createJobLevel(
                        UUID.fromString("bbbbbbbb-1111-2222-3333-444444444444"),
                        "Senior", "Advanced position requiring significant experience"
                ),
                createJobLevel(
                        UUID.fromString("cccccccc-1111-2222-3333-444444444444"),
                        "Lead", "Leadership position with team management responsibilities"
                ),
                createJobLevel(
                        UUID.fromString("dddddddd-1111-2222-3333-444444444444"),
                        "Manager", "Management position with departmental responsibilities"
                ),
                createJobLevel(
                        UUID.fromString("eeeeeeee-1111-2222-3333-444444444444"),
                        "Director", "Executive position with strategic responsibilities"
                ),
                createJobLevel(
                        UUID.fromString("ffffffff-1111-2222-3333-444444444444"),
                        "C-Level", "Top executive position (CEO, CTO, CFO, etc.)"
                )
        );

        jobLevelRepository.saveAll(jobLevels);
    }

    private JobLevel createJobLevel(UUID id, String name, String description) {
        JobLevel jobLevel = new JobLevel();
        jobLevel.setId(id);
        jobLevel.setName(name);
        jobLevel.setDescription(description);
        jobLevel.setCreatedAt(LocalDateTime.now());
        jobLevel.setUpdatedAt(LocalDateTime.now());
        return jobLevel;
    }

    private void seedSalaryRanges() {
        if (salaryRangeRepository.count() > 0) {
            return;
        }

        List<SalaryRange> salaryRanges = Arrays.asList(
                createSalaryRange(
                        UUID.fromString("12121212-1212-1212-1212-121212121212"),
                        new BigDecimal("500"), new BigDecimal("1000"), "USD", false, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("23232323-2323-2323-2323-232323232323"),
                        new BigDecimal("1000"), new BigDecimal("2000"), "USD", false, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("34343434-3434-3434-3434-343434343434"),
                        new BigDecimal("2000"), new BigDecimal("3500"), "USD", false, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("45454545-4545-4545-4545-454545454545"),
                        new BigDecimal("3500"), new BigDecimal("5000"), "USD", false, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("56565656-5656-5656-5656-565656565656"),
                        new BigDecimal("5000"), new BigDecimal("8000"), "USD", true, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("67676767-6767-6767-6767-676767676767"),
                        new BigDecimal("8000"), new BigDecimal("12000"), "USD", true, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("78787878-7878-7878-7878-787878787878"),
                        new BigDecimal("12000"), new BigDecimal("18000"), "USD", true, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("89898989-8989-8989-8989-898989898989"),
                        new BigDecimal("18000"), new BigDecimal("25000"), "USD", true, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("90909090-9090-9090-9090-909090909090"),
                        new BigDecimal("25000"), new BigDecimal("40000"), "USD", true, SalaryRange.PaymentPeriod.MONTHLY
                ),
                createSalaryRange(
                        UUID.fromString("12345678-1234-1234-1234-123456789012"),
                        new BigDecimal("15"), new BigDecimal("25"), "USD", false, SalaryRange.PaymentPeriod.HOURLY
                ),
                createSalaryRange(
                        UUID.fromString("23456789-2345-2345-2345-234567890123"),
                        new BigDecimal("25"), new BigDecimal("50"), "USD", false, SalaryRange.PaymentPeriod.HOURLY
                ),
                createSalaryRange(
                        UUID.fromString("34567890-3456-3456-3456-345678901234"),
                        new BigDecimal("50"), new BigDecimal("100"), "USD", true, SalaryRange.PaymentPeriod.HOURLY
                )
        );

        salaryRangeRepository.saveAll(salaryRanges);
    }

    private SalaryRange createSalaryRange(UUID id, BigDecimal minSalary, BigDecimal maxSalary, String currency, boolean isNegotiable, SalaryRange.PaymentPeriod paymentPeriod) {
        SalaryRange salaryRange = new SalaryRange();
        salaryRange.setId(id);
        salaryRange.setMinSalary(minSalary);
        salaryRange.setMaxSalary(maxSalary);
        salaryRange.setCurrency(currency);
        salaryRange.setNegotiable(isNegotiable);
        salaryRange.setPaymentPeriod(paymentPeriod);
        salaryRange.setCreatedAt(LocalDateTime.now());
        salaryRange.setUpdatedAt(LocalDateTime.now());
        return salaryRange;
    }

    private void seedWorkTypes() {
        if (workTypeRepository.count() > 0) {
            return;
        }

        List<WorkType> workTypes = Arrays.asList(
                createWorkType(
                        UUID.fromString("67676767-6767-6767-6767-676767676767"),
                        "Full-time", "Standard 40-hour work week"
                ),
                createWorkType(
                        UUID.fromString("34567890-3456-3456-3456-345678901234"),
                        "Part-time", "Less than 40 hours per week"
                ),
                createWorkType(
                        UUID.fromString("23456789-2345-2345-2345-234567890123"),
                        "Contract", "Fixed term employment contract"
                ),
                createWorkType(
                        UUID.fromString("12345678-1234-1234-1234-123456789012"),
                        "Temporary", "Short-term employment"
                ),
                createWorkType(
                        UUID.fromString("90909090-9090-9090-9090-909090909090"),
                        "Remote", "Work from anywhere"
                ),
                createWorkType(
                        UUID.fromString("78787878-7878-7878-7878-787878787878"),
                        "Hybrid", "Combination of in-office and remote work"
                ),
                createWorkType(
                        UUID.fromString("56565656-5656-5656-5656-565656565656"),
                        "Internship", "Temporary position for students or recent graduates"
                ),
                createWorkType(
                        UUID.fromString("34343434-3434-3434-3434-343434343434"),
                        "Freelance", "Independent contractor work"
                )
        );

        workTypeRepository.saveAll(workTypes);
    }

    private WorkType createWorkType(UUID id, String name, String description) {
        WorkType workType = new WorkType();
        workType.setId(id);
        workType.setName(name);
        workType.setDescription(description);
        workType.setCreatedAt(LocalDateTime.now());
        workType.setUpdatedAt(LocalDateTime.now());
        return workType;
    }
}