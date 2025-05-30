package com.recruitment.job_service.mapper;

import com.recruitment.job_service.dto.event.JobEvent;
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
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "industry",  ignore = true)
    @Mapping(target = "jobLevel",  ignore = true)
    @Mapping(target = "experienceLevel", ignore = true)
    @Mapping(target = "salaryRange",  ignore = true)
    @Mapping(target = "workType",  ignore = true)
    Job toEntity(JobCreationRequest jobCreationRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employerId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "industry",  ignore = true)
    @Mapping(target = "jobLevel",  ignore = true)
    @Mapping(target = "experienceLevel", ignore = true)
    @Mapping(target = "salaryRange",  ignore = true)
    @Mapping(target = "workType",  ignore = true)
    Job toEntity(JobUpdateRequest jobUpdateRequest);

    @Mapping(target = "jobLevelId", source = "jobLevel.id")
    @Mapping(target = "experienceLevelId", source = "experienceLevel.id")
    @Mapping(target = "salaryRangeId", source = "salaryRange.id")
    @Mapping(target = "workTypeId", source = "workType.id")
    @Mapping(target = "industryId", source = "industry.id")
    JobResponse toResponse(Job job);

    @Mapping(target = "jobLevel", source = "jobLevel.name")
    @Mapping(target = "experienceLevel", source = "experienceLevel.name")
    @Mapping(target = "salaryRange", source = "salaryRange.minSalary")
    @Mapping(target = "workType", source = "workType.name")
    @Mapping(target = "industryName", source = "industry.name")
    JobEvent mapToJobEvent(Job job);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employerId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "industry",  ignore = true)
    @Mapping(target = "jobLevel",  ignore = true)
    @Mapping(target = "experienceLevel", ignore = true)
    @Mapping(target = "salaryRange",  ignore = true)
    @Mapping(target = "workType",  ignore = true)
    void updateEntityFromRequest(JobUpdateRequest jobUpdateRequest, @MappingTarget Job job);
}
