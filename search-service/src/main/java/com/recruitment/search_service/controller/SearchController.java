package com.recruitment.search_service.controller;

import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.service.JobSearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final JobSearchService jobService;

    @GetMapping("/jobs")
    public Page<JobDocument> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        log.info("HttpServletRequest: {}", request.getHeader("Authorization"));
        return jobService.searchJobs(keyword, location, industry, page, size,request);
    }

    // Hoặc sử dụng method linh hoạt
    @GetMapping("/jobs/flexible")
    public Page<JobDocument> searchJobsFlexible(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return jobService.searchJobsFlexible(keyword, location, industry, page, size);
    }
}
