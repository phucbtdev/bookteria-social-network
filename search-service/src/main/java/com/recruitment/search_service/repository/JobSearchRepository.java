package com.recruitment.search_service.repository;


import com.recruitment.search_service.document.JobDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface JobSearchRepository extends ElasticsearchRepository<JobDocument, UUID> {
    Page<JobDocument> findByTitleContainingOrDescriptionContainingOrSkillsRequiredContaining(
            String title, String description, String skills, Pageable pageable);

    Page<JobDocument> findByAddressContaining(String address, Pageable pageable);

    Page<JobDocument> findByIndustryContaining(String industryName, Pageable pageable);

    Page<JobDocument> findByTitleContainingOrDescriptionContainingOrSkillsRequiredContainingAndAddressContaining(
            String title, String description, String skills, String address, Pageable pageable);

    // Sử dụng @Query annotation cho truy vấn phức tạp
    @Query("""
        {
            "bool": {
                "must": [
                    {
                        "multi_match": {
                            "query": "?0",
                            "fields": ["title", "description", "skillsRequired"]
                        }
                    }
                ],
                "filter": [
                    {
                        "match": {
                            "address": "?1"
                        }
                    },
                    {
                        "match": {
                            "industryName": "?2"
                        }
                    }
                ]
            }
        }
        """)
    Page<JobDocument> searchJobsWithAllFilters(String keyword, String address, String industryName, Pageable pageable);

    // Method linh hoạt hơn với @Query
    @Query("""
        {
            "bool": {
                "must": [
                    {
                        "bool": {
                            "should": [
                                {
                                    "multi_match": {
                                        "query": "?0",
                                        "fields": ["title^2", "description", "skillsRequired"],
                                        "type": "best_fields"
                                    }
                                }
                            ],
                            "minimum_should_match": "?#{#keyword != null && !#keyword.isEmpty() ? 1 : 0}"
                        }
                    }
                ],
                "filter": [
                    {
                        "bool": {
                            "should": [
                                {
                                    "match": {
                                        "address": "?1"
                                    }
                                }
                            ],
                            "minimum_should_match": "?#{#location != null && !#location.isEmpty() ? 1 : 0}"
                        }
                    },
                    {
                        "bool": {
                            "should": [
                                {
                                    "match": {
                                        "industryName": "?2"
                                    }
                                }
                            ],
                            "minimum_should_match": "?#{#industry != null && !#industry.isEmpty() ? 1 : 0}"
                        }
                    }
                ]
            }
        }
        """)
    Page<JobDocument> searchJobsFlexible(String keyword, String location, String industry, Pageable pageable);
}