package com.recruitment.job_service.controller;

import com.recruitment.job_service.dto.request.JobPostRequest;
import com.recruitment.job_service.dto.response.ApiResponse;
import com.recruitment.job_service.dto.response.JobPostResponse;
import com.recruitment.job_service.dto.response.PageResponse;
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
    ApiResponse<PageResponse<JobPostResponse>> getJobList(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return ApiResponse.<PageResponse<JobPostResponse>>builder()
                .result(jobPostService.getAllJobPosts(pageNo,pageSize,sortBy,sortDir)).build();
    }
}
