package com.recruitment.job_service.mapper;

import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.entity.Job;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobMapper {
    JobResponse entityToJobResponse(Job job);
}
