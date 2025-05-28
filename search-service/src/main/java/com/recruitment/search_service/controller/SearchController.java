package com.recruitment.search_service.controller;

import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.dto.JobSearchRequest;
import com.recruitment.search_service.service.JobSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SearchController {

    private final JobSearchService jobSearchService;

    @GetMapping("/jobs")
    public ResponseEntity<Page<JobDocument>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobDocument> jobs = jobSearchService.searchJobs(keyword, pageable);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/jobs/advanced")
    public ResponseEntity<Page<JobDocument>> advancedSearch(
            @RequestBody JobSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Handle different search criteria
        if (request.getIndustryId() != null) {
            return ResponseEntity.ok(jobSearchService.searchJobsByIndustry(
                    request.getKeyword(), request.getIndustryId(), pageable));
        }

        if (request.getLatitude() != null && request.getLongitude() != null) {
            return ResponseEntity.ok(jobSearchService.findJobsNearLocation(
                    request.getKeyword(), request.getDistance(),
                    request.getLatitude(), request.getLongitude(), pageable));
        }

        if (request.getSkills() != null && !request.getSkills().isEmpty()) {
            return ResponseEntity.ok(jobSearchService.findJobsBySkills(
                    request.getSkills(), pageable));
        }

        // Default to general search
        return ResponseEntity.ok(jobSearchService.searchJobs(request.getKeyword(), pageable));
    }

    @GetMapping("/jobs/{slug}")
    public ResponseEntity<JobDocument> getJobBySlug(@PathVariable String slug) {
        Optional<JobDocument> job = jobSearchService.findJobBySlug(slug);
        return job.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/jobs/employer/{employerId}")
    public ResponseEntity<List<JobDocument>> getJobsByEmployer(@PathVariable UUID employerId) {
        List<JobDocument> jobs = jobSearchService.findJobsByEmployer(employerId);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobs/skills")
    public ResponseEntity<Page<JobDocument>> searchJobsBySkills(
            @RequestParam List<String> skills,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobDocument> jobs = jobSearchService.findJobsBySkills(skills, pageable);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobs/location")
    public ResponseEntity<Page<JobDocument>> searchJobsByLocation(
            @RequestParam(required = false) String keyword,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10") Double distance,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<JobDocument> jobs = jobSearchService.findJobsNearLocation(
                keyword, distance, latitude, longitude, pageable);
        return ResponseEntity.ok(jobs);
    }
}