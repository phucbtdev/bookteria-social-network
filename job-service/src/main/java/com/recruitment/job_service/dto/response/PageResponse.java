package com.recruitment.job_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
    int pageNo;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean last;
    @Builder.Default
    List<T> data = Collections.emptyList();
}
