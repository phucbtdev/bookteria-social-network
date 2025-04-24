package com.recruitment.job_service.repository;

import com.recruitment.job_service.entity.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, String> {
    Page<JobPost> findAllByUserId(String userId,Pageable pageable);
}
