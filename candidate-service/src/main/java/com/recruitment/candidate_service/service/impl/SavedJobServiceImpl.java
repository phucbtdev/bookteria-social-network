package com.recruitment.candidate_service.service.impl;

import com.recruitment.candidate_service.dto.request.SavedJobCreationRequest;
import com.recruitment.candidate_service.dto.response.SavedJobResponse;
import com.recruitment.candidate_service.entity.SavedJob;
import com.recruitment.candidate_service.mapper.SavedJobMapper;
import com.recruitment.candidate_service.repository.SavedJobRepository;
import com.recruitment.candidate_service.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.DuplicateResourceException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    SavedJobRepository savedJobRepository;
    SavedJobMapper savedJobMapper;

    @Override
    public SavedJobResponse createSavedJob(SavedJobCreationRequest request) {
        // Check if job is already saved by candidate
        if (savedJobRepository.existsByCandidateIdAndJobPostId(request.getCandidateId(), request.getJobPostId())) {
            throw new DuplicateResourceException("Job is already saved by this candidate");
        }

        SavedJob savedJob = savedJobMapper.toEntity(request);
        SavedJob savedEntity = savedJobRepository.save(savedJob);
        return savedJobMapper.toResponse(savedEntity);
    }

    @Override
    public void deleteSavedJob(UUID id) {
        if (!savedJobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Saved job not found with id: " + id);
        }
        savedJobRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SavedJobResponse> getAllSavedJobs(Pageable pageable) {
        Page<SavedJob> savedJobs = savedJobRepository.findAll(pageable);
        return savedJobs.map(savedJobMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SavedJobResponse> getSavedJobsByCandidate(UUID candidate, Pageable pageable) {
        Page<SavedJob> savedJobs = savedJobRepository.findByCandidateOrderBySavedAtDesc(candidate, pageable);
        return savedJobs.map(savedJobMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SavedJobResponse getSavedJobById(UUID id) {
        SavedJob savedJob = savedJobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Saved job not found with id: " + id));
        return savedJobMapper.toResponse(savedJob);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isJobSavedByCandidate(UUID candidate, UUID jobPostId) {
        return savedJobRepository.existsByCandidateIdAndJobPostId(candidate, jobPostId);
    }
}
