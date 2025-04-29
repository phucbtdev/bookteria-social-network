package com.recruitment.employer_service.repository;

import com.recruitment.employer_service.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, UUID> {
}
