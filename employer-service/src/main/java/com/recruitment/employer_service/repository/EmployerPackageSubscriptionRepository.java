package com.recruitment.employer_service.repository;

import com.recruitment.employer_service.entity.EmployerPackageSubscriptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployerPackageSubscriptionRepository extends JpaRepository<EmployerPackageSubscriptions, UUID> {
}
