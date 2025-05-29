package com.recruitment.job_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange jobExchange() {
        return new DirectExchange("job.exchange");
    }

    @Bean
    public Queue jobCreatedQueue() {
        return QueueBuilder.durable("job.created.queue")
                .deadLetterExchange("job.dlx")
                .deadLetterRoutingKey("job.created.dlq")
                .build();
    }

    @Bean
    public Queue jobUpdatedQueue() {
        return QueueBuilder.durable("job.updated.queue")
                .deadLetterExchange("job.dlx")
                .deadLetterRoutingKey("job.updated.dlq")
                .build();
    }

    @Bean
    public Queue jobDeletedQueue() {
        return QueueBuilder.durable("job.deleted.queue")
                .deadLetterExchange("job.dlx")
                .deadLetterRoutingKey("job.deleted.dlq")
                .build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("job.dlx");
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue("job.dlq.queue", true);
    }

    @Bean
    public Binding createdBinding(Queue jobCreatedQueue, DirectExchange jobExchange) {
        return BindingBuilder.bind(jobCreatedQueue).to(jobExchange).with("job.created");
    }

    @Bean
    public Binding updatedBinding(Queue jobUpdatedQueue, DirectExchange jobExchange) {
        return BindingBuilder.bind(jobUpdatedQueue).to(jobExchange).with("job.updated");
    }

    @Bean
    public Binding deletedBinding(Queue jobDeletedQueue, DirectExchange jobExchange) {
        return BindingBuilder.bind(jobDeletedQueue).to(jobExchange).with("job.deleted");
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("#");
    }
}
