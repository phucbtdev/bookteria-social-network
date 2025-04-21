package com.recruitment.notification_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    BrevoRecipient to;
    String subject;
    String htmlContent;
}
