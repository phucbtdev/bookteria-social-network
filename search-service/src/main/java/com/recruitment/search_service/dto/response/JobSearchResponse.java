package com.recruitment.search_service.dto.response;

import com.recruitment.search_service.document.JobDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class JobSearchResponse {
    List<JobDocument> jobs;
    long totalHits;
    int page;
    int size;
    int totalPages;

    public JobSearchResponse(List<JobDocument> jobs, long totalHits, int page, int size) {
    }
}
