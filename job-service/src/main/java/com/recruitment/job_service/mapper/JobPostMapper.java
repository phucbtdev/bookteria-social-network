package com.recruitment.job_service.mapper;

import com.recruitment.job_service.dto.response.JobPostResponse;
import com.recruitment.job_service.entity.JobPost;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobPostMapper {
    JobPostResponse toJobPostResponse(JobPost jobPost);
}
