package com.recruitment.employer_service.repository;

import com.recruitment.employer_service.entity.EmployerPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployerPackageRepository extends JpaRepository<EmployerPackage, UUID> {
    EmployerPackage findByName(String name);
}
