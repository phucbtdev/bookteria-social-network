package com.recruitment.search_service.service.kafka;

import com.recruitment.common.event.JobDeletedEvent;
import com.recruitment.common.event.JobEvent;
import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.repository.JobSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobEventConsumer {

    private final JobSearchRepository repository;

    @KafkaListener(topics = "${job.kafka.topic.created-updated}", groupId = "search-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleJobCreatedAndUpdated(JobEvent event) {
        JobDocument doc = mapToJobDocument(event);
        repository.save(doc);
    }

    @KafkaListener(topics = "${job.kafka.topic.deleted}", groupId = "search-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleJobDeleted(JobDeletedEvent event) {
        repository.deleteById(event.getId());
    }

    private JobDocument mapToJobDocument(JobEvent event) {
        return JobDocument.builder()
                .id(UUID.fromString(event.getId()))
                .employerId(event.getEmployerId())
                .title(event.getTitle())
                .slug(event.getSlug())
                .description(event.getDescription())
                .industry(event.getIndustryName())
                .jobLevel(event.getJobLevel())
                .experienceLevel(event.getExperienceLevel())
                .salaryRange(event.getSalaryRange())
                .workType(event.getWorkType())
                .numberOfPositions(event.getNumberOfPositions())
                .skillsRequired(event.getSkillsRequired())
                .genderRequirement(event.getGenderRequirement())
                .address(event.getAddress())
                .status(event.getStatus())
                .build();
    }
}
