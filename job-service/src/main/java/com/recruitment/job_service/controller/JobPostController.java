package com.recruitment.job_service.controller;

import com.recruitment.job_service.dto.request.JobPostRequest;
import com.recruitment.job_service.dto.response.ApiResponse;
import com.recruitment.job_service.dto.response.JobPostResponse;
import com.recruitment.job_service.service.JobPostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobPostController {
JobPostService jobPostService;
    @PostMapping()
    ApiResponse<JobPostResponse> postJob(@RequestBody JobPostRequest jobPostRequest ) {
        return ApiResponse.<JobPostResponse>builder()
                .result(jobPostService.createJob(jobPostRequest)).build();
    }

    @GetMapping()
    ApiResponse<List<JobPostResponse>> getJobList( ) {
        return ApiResponse.<List<JobPostResponse>>builder()
                .result(jobPostService.getAllJobPosts()).build();
    }

}
