package com.recruitment.job_service.mapper;

import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "username", ignore = true)
    JobResponse entityToJobResponse(Job job);
}
