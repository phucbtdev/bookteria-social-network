package com.recruitment.notification_service.controller;

import com.recruitment.event.dto.NotificationEvent;
import com.recruitment.notification_service.dto.request.BrevoRecipient;
import com.recruitment.notification_service.dto.request.SendEmailRequest;
import com.recruitment.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-delivery1")
    public void listenNotificationDelivery(NotificationEvent message){
        log.info("NotificationEvent :{}", message);
        emailService.sendMail(SendEmailRequest.builder()
                        .to(BrevoRecipient.builder()
                                .email(message.getRecipient())
                                .name("Test")
                                .build())
                        .subject(message.getSubject())
                        .htmlContent(message.getBody())
                .build());
    }
}
