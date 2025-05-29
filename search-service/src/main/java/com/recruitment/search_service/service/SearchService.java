package com.recruitment.search_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.dto.event.JobCreatedEvent;
import com.recruitment.search_service.dto.event.JobDeletedEvent;
import com.recruitment.search_service.dto.event.JobUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final ElasticsearchClient elasticsearchClient;

    public void indexJob(JobCreatedEvent event) throws IOException {
        JobDocument document = new JobDocument();
        document.setId(event.id());
        document.setEmployerId(event.employerId());
        document.setTitle(event.title());
        document.setSlug(event.slug());
        document.setDescription(event.description());
        document.setIndustryId(event.industryId());
        document.setJobLevelId(event.jobLevelId());
        document.setExperienceLevelId(event.experienceLevelId());
        document.setSalaryRangeId(event.salaryRangeId());
        document.setWorkTypeId(event.workTypeId());
        document.setNumberOfPositions(event.numberOfPositions());
        document.setSkillsRequired(event.skillsRequired());
        document.setGenderRequirement(event.genderRequirement());
        document.setAddress(event.address());
        document.setApplicationDeadline(event.applicationDeadline());
        document.setStatus(event.status());

        IndexRequest<JobDocument> request = IndexRequest.of(i -> i
                .index("jobs")
                .id(document.getId().toString())
                .document(document)
        );
        elasticsearchClient.index(request);
    }

    public void updateJob(JobUpdatedEvent event) throws IOException {
        JobDocument document = new JobDocument();
        document.setId(event.id());
        document.setTitle(event.title());
        document.setSlug(event.slug());
        document.setDescription(event.description());
        document.setIndustryId(event.industryId());
        document.setJobLevelId(event.jobLevelId());
        document.setExperienceLevelId(event.experienceLevelId());
        document.setSalaryRangeId(event.salaryRangeId());
        document.setWorkTypeId(event.workTypeId());
        document.setNumberOfPositions(event.numberOfPositions());
        document.setSkillsRequired(event.skillsRequired());
        document.setGenderRequirement(event.genderRequirement());
        document.setAddress(event.address());
        document.setApplicationDeadline(event.applicationDeadline());
        document.setStatus(event.status());

        UpdateRequest<JobDocument, JobDocument> request = UpdateRequest.of(u -> u
                .index("jobs")
                .id(event.id().toString())
                .doc(document)
        );
        elasticsearchClient.update(request, JobDocument.class);
    }

    public void deleteJob(JobDeletedEvent event) throws IOException {
        DeleteRequest request = DeleteRequest.of(d -> d
                .index("jobs")
                .id(String.valueOf(event.id()))
        );
        elasticsearchClient.delete(request);
    }
}