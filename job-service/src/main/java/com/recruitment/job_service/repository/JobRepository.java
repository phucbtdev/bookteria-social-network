package com.recruitment.job_service.repository;

import com.recruitment.job_service.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    Page<Job> findAllByUserId(String userId, Pageable pageable);
}
