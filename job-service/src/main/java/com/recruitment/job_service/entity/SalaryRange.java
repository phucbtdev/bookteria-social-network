package com.recruitment.job_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "salary_ranges")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalaryRange extends BaseEntity {

    @Column(name = "min_salary", precision = 19, scale = 2)
    BigDecimal minSalary;

    @Column(name = "max_salary", precision = 19, scale = 2)
    BigDecimal maxSalary;

    @Column(name = "currency", length = 3)
    String currency; // USD, VND, EUR...

    @Column(name = "is_negotiable")
    boolean isNegotiable;

    @Column(name = "payment_period")
    @Enumerated(EnumType.STRING)
    PaymentPeriod paymentPeriod; // HOURLY, MONTHLY, YEARLY

    @Builder.Default
    @OneToMany(mappedBy = "salaryRange")
    Set<Job> jobs = new HashSet<>();

    public enum PaymentPeriod {
        HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY
    }
}
