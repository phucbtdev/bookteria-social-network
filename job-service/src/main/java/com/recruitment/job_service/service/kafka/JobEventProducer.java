package com.recruitment.job_service.service.kafka;

import com.recruitment.job_service.dto.event.JobDeletedEvent;
import com.recruitment.job_service.dto.event.JobEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobEventProducer {
    private final KafkaTemplate<UUID, Object> kafkaTemplate;

    @Value("${job.kafka.topic.created}")
    private String jobCreatedTopic;

    @Value("${job.kafka.topic.updated}")
    private String jobUpdatedTopic;

    @Value("${job.kafka.topic.deleted}")
    private String jobDeletedTopic;

    public void sendJobCreatedEvent(JobEvent event) {
        kafkaTemplate.send(jobCreatedTopic, event.getId(), event);
    }
    public void sendJobUpdatedEvent(JobEvent event) {
        kafkaTemplate.send(jobUpdatedTopic, event.getId(), event);
    }
    public void sendJobDeletedEvent(JobDeletedEvent event) {
        kafkaTemplate.send(jobDeletedTopic, event.getId(), event);
    }
}

