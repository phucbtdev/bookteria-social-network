package com.recruitment.notification_service.repository.httpclient;

import com.recruitment.notification_service.dto.request.BrevoEmailRequest;
import com.recruitment.notification_service.dto.response.BrevoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "notification-service",
        url = "https://api.brevo.com"
)
public interface BrevoMailRepository {
    @PostMapping(value ="/v3/smtp/email",produces = MediaType.APPLICATION_JSON_VALUE)
    BrevoResponse sendMail(@RequestHeader("api-key") String apiKey, @RequestBody BrevoEmailRequest brevoEmailRequest);
}
