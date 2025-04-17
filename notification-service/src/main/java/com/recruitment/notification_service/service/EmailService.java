package com.recruitment.notification_service.service;

import com.recruitment.notification_service.dto.request.BrevoEmailRequest;
import com.recruitment.notification_service.dto.request.BrevoSender;
import com.recruitment.notification_service.dto.request.SendEmailRequest;
import com.recruitment.notification_service.dto.response.BrevoResponse;
import com.recruitment.notification_service.exception.AppException;
import com.recruitment.notification_service.exception.ErrorCode;
import com.recruitment.notification_service.repository.httpclient.BrevoMailRepository;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    BrevoMailRepository brevoMailRepository;

    @Value("${brevo.api.key}")
    @NonFinal
    String apiKey;

    public BrevoResponse sendMail(SendEmailRequest sendEmailRequest){
        BrevoEmailRequest brevoEmailRequest = BrevoEmailRequest.builder()
                .sender(BrevoSender.builder()
                        .name("Loid Dev")
                        .email("trongphuceazy@gmail.com")
                        .build())
                .to(List.of(sendEmailRequest.getTo()))
                .subject(sendEmailRequest.getSubject())
                .htmlContent(sendEmailRequest.getHtmlContent())
                .build();

        try {
            return brevoMailRepository.sendMail(apiKey,brevoEmailRequest);
        } catch (FeignException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
