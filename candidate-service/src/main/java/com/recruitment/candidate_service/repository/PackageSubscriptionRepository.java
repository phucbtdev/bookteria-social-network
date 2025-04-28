package com.recruitment.candidate_service.repository;

import com.recruitment.candidate_service.entity.CandidatePackageSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PackageSubscriptionRepository extends JpaRepository<CandidatePackageSubscription, UUID> {
}
