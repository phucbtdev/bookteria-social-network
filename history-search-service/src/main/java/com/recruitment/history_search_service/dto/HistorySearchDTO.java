package com.recruitment.history_search_service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistorySearchDTO {
    private UUID id;
    private String userId;
    private String keyword;
    private String location;
    private String industry;
    private Integer page;
    private Integer size;
    private Instant searchTime;
    private String ipAddress;
    private String userAgent;

}
