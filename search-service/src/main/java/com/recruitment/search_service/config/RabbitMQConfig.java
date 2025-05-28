package com.recruitment.search_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.job-events}")
    private String jobEventsExchange;

    @Value("${rabbitmq.queue.job-events}")
    private String jobEventsQueue;

    @Value("${rabbitmq.routing-key.job-events}")
    private String jobEventsRoutingKey;

    @Bean
    public TopicExchange jobEventsExchange() {
        return new TopicExchange(jobEventsExchange, true, false);
    }

    @Bean
    public Queue jobEventsQueue() {
        return QueueBuilder.durable(jobEventsQueue)
                .withArgument("x-dead-letter-exchange", jobEventsExchange + ".dlx")
                .withArgument("x-dead-letter-routing-key", jobEventsRoutingKey + ".dlq")
                .build();
    }

    @Bean
    public Binding jobEventsBinding() {
        return BindingBuilder
                .bind(jobEventsQueue())
                .to(jobEventsExchange())
                .with(jobEventsRoutingKey);
    }

    // Dead Letter Queue for failed messages
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(jobEventsExchange + ".dlx", true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(jobEventsQueue + ".dlq").build();
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(jobEventsRoutingKey + ".dlq");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}