package com.recruitment.search_service.repository;


import com.recruitment.search_service.document.JobDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface JobDocumentRepository extends ElasticsearchRepository<JobDocument, UUID> {
}