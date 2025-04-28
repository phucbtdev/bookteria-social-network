package com.recruitment.candidate_service.repository;

import com.recruitment.candidate_service.entity.CandidatePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatePackageRepository extends JpaRepository<CandidatePackage, Integer> {
}
