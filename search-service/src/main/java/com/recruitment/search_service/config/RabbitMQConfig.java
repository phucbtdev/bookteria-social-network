package com.recruitment.search_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

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
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("#");
    }

}