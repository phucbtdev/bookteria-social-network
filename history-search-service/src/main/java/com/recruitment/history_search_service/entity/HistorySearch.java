package com.recruitment.history_search_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "history_search")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistorySearch {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    UUID id;

    @Column(name = "user_id", length = 36) // UUID dạng string chuẩn
    String userId;

    @Column(name = "keyword", length = 255)
    String keyword;

    @Column(name = "location", length = 255)
    String location;

    @Column(name = "industry", length = 255)
    String industry;

    @Column(name = "page")
    Integer page;

    @Column(name = "size")
    Integer size;

    @Column(name = "search_time", nullable = false, updatable = false)
    Instant searchTime;

    @Column(name = "ip_address", length = 45)
    String ipAddress;

    @Column(name = "user_agent", length = 255)
    String userAgent;

    @PrePersist
    public void prePersist() {
        this.searchTime = Instant.now();
    }


}

