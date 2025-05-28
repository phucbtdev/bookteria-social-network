package com.recruitment.search_service.repository;

import com.recruitment.search_service.document.JobDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobRepository extends ElasticsearchRepository<JobDocument, String> {

    Optional<JobDocument> findByJobId(UUID jobId);

    List<JobDocument> findByEmployerId(UUID employerId);

    Page<JobDocument> findByStatus(String status, Pageable pageable);

    @Query("""
        {
          "bool": {
            "must": [
              {
                "multi_match": {
                  "query": "?0",
                  "fields": ["title^3", "description^2", "skills^2", "address"],
                  "type": "best_fields",
                  "fuzziness": "AUTO"
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "status": "APPROVED"
                }
              },
              {
                "range": {
                  "applicationDeadline": {
                    "gte": "now/d"
                  }
                }
              }
            ]
          }
        }
        """)
    Page<JobDocument> searchActiveJobs(String keyword, Pageable pageable);

    @Query("""
        {
          "bool": {
            "must": [
              {
                "multi_match": {
                  "query": "?0",
                  "fields": ["title^3", "description^2", "skills^2"]
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "status": "APPROVED"
                }
              },
              {
                "range": {
                  "applicationDeadline": {
                    "gte": "now/d"
                  }
                }
              },
              {
                "nested": {
                  "path": "industry",
                  "query": {
                    "term": {
                      "industry.id": "?1"
                    }
                  }
                }
              }
            ]
          }
        }
        """)
    Page<JobDocument> searchJobsByIndustry(String keyword, UUID industryId, Pageable pageable);

    @Query("""
        {
          "bool": {
            "must": [
              {
                "geo_distance": {
                  "distance": "?1km",
                  "location": {
                    "lat": ?2,
                    "lon": ?3
                  }
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "status": "APPROVED"
                }
              },
              {
                "range": {
                  "applicationDeadline": {
                    "gte": "now/d"
                  }
                }
              }
            ]
          }
        }
        """)
    Page<JobDocument> findJobsNearLocation(String keyword, Double distance, Double lat, Double lon, Pageable pageable);

    @Query("""
        {
          "bool": {
            "must": [
              {
                "terms": {
                  "skills": ?0
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "status": "APPROVED"
                }
              },
              {
                "range": {
                  "applicationDeadline": {
                    "gte": "now/d"
                  }
                }
              }
            ]
          }
        }
        """)
    Page<JobDocument> findJobsBySkills(List<String> skills, Pageable pageable);

    void deleteByJobId(UUID jobId);
}