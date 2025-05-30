package com.recruitment.search_service.controller;

import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.service.JobSearchService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final JobSearchService jobService;

    @GetMapping("/jobs")
    public Page<JobDocument> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return jobService.searchJobs(keyword, location, industry, page, size);
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
