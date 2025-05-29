package com.recruitment.search_service.listener;

import com.rabbitmq.client.Channel;
import com.recruitment.search_service.dto.event.JobCreatedEvent;
import com.recruitment.search_service.dto.event.JobDeletedEvent;
import com.recruitment.search_service.dto.event.JobUpdatedEvent;
import com.recruitment.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobEventListener {

    private final SearchService searchService;

    @RabbitListener(queues = "job.created.queue")
    public void handleJobCreatedEvent(JobCreatedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            searchService.indexJob(event);
            channel.basicAck(tag, false);
        } catch (IOException e) {
            channel.basicNack(tag, false, false); // Gửi đến DLQ
            throw new RuntimeException("Failed to index job", e);
        }
    }

    @RabbitListener(queues = "job.updated.queue")
    public void handleJobUpdatedEvent(JobUpdatedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            searchService.updateJob(event);
            channel.basicAck(tag, false);
        } catch (IOException e) {
            channel.basicNack(tag, false, false); // Gửi đến DLQ
            throw new RuntimeException("Failed to update job", e);
        }
    }

    @RabbitListener(queues = "job.deleted.queue")
    public void handleJobDeletedEvent(JobDeletedEvent event, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            searchService.deleteJob(event);
            channel.basicAck(tag, false);
        } catch (IOException e) {
            channel.basicNack(tag, false, false); // Gửi đến DLQ
            throw new RuntimeException("Failed to delete job", e);
        }
    }
}