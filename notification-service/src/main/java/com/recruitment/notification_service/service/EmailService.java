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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
                        .email("phucbuitrong.dev@gmail.com")
                        .build())
                .to(List.of(sendEmailRequest.getTo()))
                .subject(sendEmailRequest.getSubject())
                .htmlContent(sendEmailRequest.getHtmlContent())
                .build();

        log.info("BrevoEmailRequest :{}", brevoEmailRequest);

        try {
            return brevoMailRepository.sendMail(apiKey,brevoEmailRequest);
        } catch (FeignException ex) {
            log.error("Lỗi khi gửi email: {}", ex.getMessage(), ex);
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
