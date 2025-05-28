package com.recruitment.search_service.handler;

import com.recruitment.search_service.event.JobEvent;
import com.recruitment.search_service.service.JobSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobEventHandler {

    private final JobSearchService jobSearchService;

    @RabbitListener(queues = "${rabbitmq.queue.job-events}")
    public void handleJobEvent(JobEvent event) {
        log.info("Received job event: {} for job ID: {}", event.getEventType(), event.getJobId());

        try {
            switch (event.getEventType()) {
                case JOB_CREATED -> {
                    log.info("Processing job creation event for job: {}", event.getJobId());
                    jobSearchService.indexJob(event.getJobData());
                }
                case JOB_UPDATED -> {
                    log.info("Processing job update event for job: {}", event.getJobId());
                    jobSearchService.updateJob(event.getJobData());
                }
                case JOB_DELETED -> {
                    log.info("Processing job deletion event for job: {}", event.getJobId());
                    jobSearchService.deleteJob(event.getJobId());
                }
                case JOB_STATUS_CHANGED -> {
                    log.info("Processing job status change event for job: {}", event.getJobId());
                    jobSearchService.updateJobStatus(event.getJobId(), event.getJobData().getStatus());
                }
                default -> log.warn("Unknown event type: {}", event.getEventType());
            }

            log.info("Successfully processed job event: {} for job ID: {}",
                    event.getEventType(), event.getJobId());

        } catch (Exception e) {
            log.error("Error processing job event: {} for job ID: {}",
                    event.getEventType(), event.getJobId(), e);
            throw e; // Re-throw to trigger retry mechanism if configured
        }
    }
}