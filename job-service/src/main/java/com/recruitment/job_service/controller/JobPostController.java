package com.recruitment.job_service.controller;

import com.recruitment.job_service.dto.request.JobPostRequest;
import com.recruitment.job_service.dto.response.ApiResponse;
import com.recruitment.job_service.dto.response.JobPostResponse;
import com.recruitment.job_service.service.JobPostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/job")
public class JobPostController {

    JobPostService jobPostService;

    @PostMapping
    ApiResponse<JobPostResponse> postJob(@RequestBody JobPostRequest request ) {
        return ApiResponse.<JobPostResponse>builder()
                .result(jobPostService.createJob(request)).build();
    }

    @GetMapping
    ApiResponse<List<JobPostResponse>> getJobList() {
        return ApiResponse.<List<JobPostResponse>>builder()
                .result(jobPostService.getAllJobPosts()).build();
    }

}
