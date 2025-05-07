package com.recruitment.candidate_service.repository;

import com.recruitment.candidate_service.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    Candidate findByUserId(String userId);
}
