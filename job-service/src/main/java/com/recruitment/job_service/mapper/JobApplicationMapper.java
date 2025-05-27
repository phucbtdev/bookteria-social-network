package com.recruitment.job_service.mapper;

import com.recruitment.job_service.dto.request.JobApplicationCreateRequest;
import com.recruitment.job_service.dto.request.JobApplicationUpdateRequest;
import com.recruitment.job_service.dto.response.JobApplicationResponse;
import com.recruitment.job_service.entity.JobApplication;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appliedAt", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "job.id", source = "jobId")
    JobApplication toEntity(JobApplicationCreateRequest request);

    @Mapping(target = "job", source = "job")
    JobApplicationResponse toResponse(JobApplication entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "candidateId", ignore = true)
    @Mapping(target = "job", ignore = true)
    @Mapping(target = "appliedAt", ignore = true)
    void updateEntity(@MappingTarget JobApplication entity, JobApplicationUpdateRequest request);
}
