package com.recruitment.notification_service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BrevoEmailRequest {
    BrevoSender sender;
    List<BrevoRecipient> to;
    String htmlContent;
    String subject;
}
