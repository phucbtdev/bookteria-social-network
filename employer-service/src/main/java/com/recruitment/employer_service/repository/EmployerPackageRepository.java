package com.recruitment.employer_service.repository;

import com.recruitment.employer_service.entity.EmployerPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerPackageRepository extends JpaRepository<EmployerPackage, Integer> {
}
