package com.recruitment.job_service.mapper;

import com.recruitment.job_service.dto.request.JobCreationRequest;
import com.recruitment.job_service.dto.request.JobUpdateRequest;
import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JobMapper {
    @Mapping(target = "id", ignore = true)
    Job toEntity(JobCreationRequest jobCreationRequest);

    @Mapping(target = "id", ignore = true)
    Job toEntity(JobUpdateRequest jobUpdateRequest);

    @Mapping(target = "username", ignore = true)
    JobResponse toResponse(Job job);

    void updateEntityFromRequest(JobUpdateRequest jobUpdateRequest, @MappingTarget Job job);
}
