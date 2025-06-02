package com.recruitment.search_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistorySearchDTO {
    UUID id;
    String userId;
    String keyword;
    String location;
    String industry;
    Integer page;
    Integer size;
    Instant searchTime;
    String ipAddress;
    String userAgent;

}
