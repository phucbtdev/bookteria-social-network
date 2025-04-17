package com.recruitment.notification_service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SendEmailRequest {
    BrevoRecipient to;
    String htmlContent;
    String subject;
}
