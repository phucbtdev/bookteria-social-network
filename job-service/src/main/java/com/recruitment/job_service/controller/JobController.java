package com.recruitment.job_service.controller;

import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.job_service.dto.request.JobCreationRequest;
import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.service.JobService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/job")
public class JobController {

    JobService jobService;

    @PostMapping
    ApiResponse<JobResponse> postJob(@RequestBody JobCreationRequest request ) {
        return ApiResponse.<JobResponse>builder()
                .result(jobService.createJob(request))
                .build();
    }

    @GetMapping
    ApiResponse<PageResponse<JobResponse>> getJobList(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return ApiResponse.<PageResponse<JobResponse>>builder()
                .result(jobService.getAllJobPosts(pageNo,pageSize,sortBy,sortDir))
                .build();
    }
}
