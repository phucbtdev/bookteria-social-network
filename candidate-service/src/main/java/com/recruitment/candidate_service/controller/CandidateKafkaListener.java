package com.recruitment.candidate_service.controller;

import com.recruitment.candidate_service.service.CandidateService;
import com.recruitment.common.dto.request.CandidateCreationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CandidateKafkaListener {

    KafkaTemplate<String, Object> kafkaTemplate;
    CandidateService candidateService;

    @KafkaListener(topics = "candidate-registration")
    public void handleCandidateRegistration(
            CandidateCreationRequest creationRequest
    ) {
        try {
            candidateService.createCandidateFromIdentity(creationRequest);
        } catch (Exception e) {
            kafkaTemplate.send("candidate-creation-failed", Map.of(
                    "userId", creationRequest.getUserId(),
                    "reason", "Exception: " + e.getMessage()
            ) );
        }
    }
}
