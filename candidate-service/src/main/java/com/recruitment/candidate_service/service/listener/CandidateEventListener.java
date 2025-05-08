package com.recruitment.candidate_service.service.listener;

import com.recruitment.candidate_service.event.CandidateCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class CandidateEventListener {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmployerCreated(CandidateCreatedEvent event) {
        kafkaTemplate.send("candidate-creation-success", Map.of(
                "userId", event.getUserId().toString(),
                "employerId", event.getCandidateId().toString()
        ));
    }
}
