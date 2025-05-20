package com.recruitment.job_service.repository;

import com.recruitment.job_service.entity.ExperienceLevel;
import com.recruitment.job_service.entity.JobLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobLevelRepository extends JpaRepository<JobLevel, UUID> {
    List<JobLevel> findAllById(Iterable<UUID> ids);
}
