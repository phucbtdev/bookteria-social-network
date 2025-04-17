package com.recruitment.notification_service.controller;

import com.recruitment.notification_service.dto.ApiResponse;
import com.recruitment.notification_service.dto.request.SendEmailRequest;
import com.recruitment.notification_service.dto.response.BrevoResponse;
import com.recruitment.notification_service.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrevoEmailController {
    EmailService emailService;

    @PostMapping("/send/mail")
    ApiResponse<BrevoResponse> sendMail(@RequestBody SendEmailRequest sendEmailRequest) {
        return ApiResponse.<BrevoResponse>builder()
                .result(emailService.sendMail(sendEmailRequest))
                .build();
    }
}
