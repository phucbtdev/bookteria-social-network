package com.recruitment.search_service.controller;

import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.dto.JobSearchRequest;
import com.recruitment.search_service.dto.response.JobSearchResponse;
import com.recruitment.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/jobs")
    public ResponseEntity<JobSearchResponse> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID industryId,
            @RequestParam(required = false) UUID jobLevelId,
            @RequestParam(required = false) UUID experienceLevelId,
            @RequestParam(required = false) UUID salaryRangeId,
            @RequestParam(required = false) UUID workTypeId,
            @RequestParam(required = false) String genderRequirement,
            @RequestParam(required = false, defaultValue = "ACTIVE") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "relevance") String sortBy
    ) {
        try {
            JobSearchResponse response = searchService.searchJobs(
                    keyword,
                    industryId, jobLevelId, experienceLevelId, salaryRangeId, workTypeId,
                    genderRequirement, status, page, size, sortBy
            );
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
