package com.recruitment.search_service.service;

import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.repository.JobSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSearchService {

    private final JobSearchRepository jobRepository;

    public Page<JobDocument> searchJobs(String keyword, String location, String industry, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Cách 1: Sử dụng method đơn giản (khuyến nghị cho logic đơn giản)
        if (keyword != null && !keyword.isEmpty() &&
                location != null && !location.isEmpty() &&
                industry != null && !industry.isEmpty()) {
            return jobRepository.searchJobsWithAllFilters(keyword, location, industry, pageable);
        }

        // Cách 2: Xử lý từng trường hợp
        if (keyword != null && !keyword.isEmpty()) {
            if (location != null && !location.isEmpty()) {
                return jobRepository.findByTitleContainingOrDescriptionContainingOrSkillsRequiredContainingAndAddressContaining(
                        keyword, keyword, keyword, location, pageable);
            }
            return jobRepository.findByTitleContainingOrDescriptionContainingOrSkillsRequiredContaining(
                    keyword, keyword, keyword, pageable);
        }

        if (location != null && !location.isEmpty()) {
            return jobRepository.findByAddressContaining(location, pageable);
        }

        if (industry != null && !industry.isEmpty()) {
            return jobRepository.findByIndustryContaining(industry, pageable);
        }

        // Trả về tất cả nếu không có filter
        return jobRepository.findAll(pageable);
    }

    // Cách 3: Sử dụng method linh hoạt nhất
    public Page<JobDocument> searchJobsFlexible(String keyword, String location, String industry, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jobRepository.searchJobsFlexible(keyword, location, industry, pageable);
    }
}
