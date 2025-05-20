package com.recruitment.job_service.repository;

import com.recruitment.job_service.entity.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkTypeRepository extends JpaRepository<WorkType, UUID> {
    List<WorkType> findAllById(Iterable<UUID> ids);
}
