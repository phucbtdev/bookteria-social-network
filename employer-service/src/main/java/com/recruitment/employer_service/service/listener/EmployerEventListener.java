package com.recruitment.employer_service.service.listener;

import com.recruitment.employer_service.event.EmployerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class EmployerEventListener {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmployerCreated(EmployerCreatedEvent event) {
        kafkaTemplate.send("employer-creation-success", Map.of(
                "userId", event.getUserId().toString(),
                "employerId", event.getEmployerId().toString(),
                "companyName", event.getCompanyName()
        ));
    }
}
