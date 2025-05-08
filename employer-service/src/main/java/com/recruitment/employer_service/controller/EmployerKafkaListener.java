package com.recruitment.employer_service.controller;

import com.recruitment.common.dto.request.EmployerCreationRequest;
import com.recruitment.employer_service.service.EmployerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerKafkaListener {
    KafkaTemplate<String, Object> kafkaTemplate;
    EmployerService employerService;

    @KafkaListener(topics = "employer-registration")
    public void handleEmployerRegistration(
            EmployerCreationRequest creationRequest
    ) {
        try {
            employerService.createEmployerFromIdentity(creationRequest);
        } catch (Exception e) {
            kafkaTemplate.send("employer-creation-failed", Map.of(
                    "userId", creationRequest.getUserId(),
                    "reason", "Exception: " + e.getMessage()
            ) );
        }
    }

}
