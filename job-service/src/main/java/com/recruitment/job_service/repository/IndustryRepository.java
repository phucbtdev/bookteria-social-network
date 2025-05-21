package com.recruitment.job_service.repository;

import com.recruitment.job_service.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, UUID> {
    List<Industry> findAllById(Iterable<UUID> ids);
}
