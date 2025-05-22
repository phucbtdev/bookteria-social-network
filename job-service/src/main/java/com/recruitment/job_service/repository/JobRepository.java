package com.recruitment.job_service.repository;

import com.recruitment.job_service.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    Page<Job> findAllByEmployerId(UUID employerId, Pageable pageable);
    boolean existsBySlug(String slug);
    Optional<Job> findBySlug(String slug);

    Page<Job> findByEmployerId(UUID employerId, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE " +
            "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:industryId IS NULL OR j.industry.id = :industryId) AND " +
            "(:jobLevelId IS NULL OR j.jobLevel.id = :jobLevelId) AND " +
            "(:experienceLevelId IS NULL OR j.experienceLevel.id = :experienceLevelId) AND " +
            "(:workTypeId IS NULL OR j.workType.id = :workTypeId) AND " +
            "(:genderRequirement IS NULL OR j.genderRequirement = :genderRequirement) AND " +
            "(:status IS NULL OR j.status = :status) AND " +
            "(:deadlineFrom IS NULL OR j.applicationDeadline >= :deadlineFrom) AND " +
            "(:deadlineTo IS NULL OR j.applicationDeadline <= :deadlineTo) AND " +
            "(:location IS NULL OR LOWER(j.address) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Job> findJobsWithFilters(
            @Param("title") String title,
            @Param("industryId") UUID industryId,
            @Param("jobLevelId") UUID jobLevelId,
            @Param("experienceLevelId") UUID experienceLevelId,
            @Param("workTypeId") UUID workTypeId,
            @Param("genderRequirement") Job.GenderRequirement genderRequirement,
            @Param("status") Job.JobPostStatus status,
            @Param("deadlineFrom") LocalDate deadlineFrom,
            @Param("deadlineTo") LocalDate deadlineTo,
            @Param("location") String location,
            Pageable pageable
    );
}
