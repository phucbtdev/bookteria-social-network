package com.recruitment.candidate_service.service;

import com.recruitment.candidate_service.dto.request.SavedJobCreationRequest;
import com.recruitment.candidate_service.dto.response.SavedJobResponse;
import com.recruitment.common.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface SavedJobService {

    SavedJobResponse createSavedJob(SavedJobCreationRequest request);

    void deleteSavedJob(UUID id);

    PageResponse<SavedJobResponse> getAllSavedJobs(Pageable pageable);

    Page<SavedJobResponse> getSavedJobsByCandidate(UUID candidate, Pageable pageable);

    SavedJobResponse getSavedJobById(UUID id);

    boolean isJobSavedByCandidate(UUID candidate, UUID jobPostId);
}
