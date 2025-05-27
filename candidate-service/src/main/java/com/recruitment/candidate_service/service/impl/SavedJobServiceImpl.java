package com.recruitment.candidate_service.service.impl;

import com.recruitment.candidate_service.dto.request.SavedJobCreationRequest;
import com.recruitment.candidate_service.dto.response.SavedJobResponse;
import com.recruitment.candidate_service.entity.Candidate;
import com.recruitment.candidate_service.entity.SavedJob;
import com.recruitment.candidate_service.exception.AppException;
import com.recruitment.candidate_service.exception.ErrorCode;
import com.recruitment.candidate_service.mapper.SavedJobMapper;
import com.recruitment.candidate_service.repository.CandidateRepository;
import com.recruitment.candidate_service.repository.SavedJobRepository;
import com.recruitment.candidate_service.service.SavedJobService;
import com.recruitment.common.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SavedJobServiceImpl implements SavedJobService {

    SavedJobRepository savedJobRepository;
    SavedJobMapper savedJobMapper;
    CandidateRepository candidateRepository;

    @Override
    public SavedJobResponse createSavedJob(SavedJobCreationRequest request) {
        if (savedJobRepository.existsByCandidateIdAndJobPostId(request.getCandidateId(), request.getJobPostId())) {
            throw new AppException(ErrorCode.JOB_ALREADY_SAVED);
        }

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new AppException(ErrorCode.CANDIDATE_NOT_EXISTED));

        SavedJob savedJob = savedJobMapper.toEntity(request);
        savedJob.setCandidate(candidate);
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
    public PageResponse<SavedJobResponse> getAllSavedJobs(Pageable pageable) {
        Page<SavedJob> savedJobs = savedJobRepository.findAll(pageable);
        List<SavedJobResponse> savedJobResponseList = savedJobs.getContent().stream().map(savedJobMapper::toResponse).toList();

        return PageResponse.<SavedJobResponse>builder()
                .pageNo(savedJobs.getNumber() + 1)
                .pageSize(savedJobs.getSize())
                .totalElements(savedJobs.getTotalElements())
                .totalPages(savedJobs.getTotalPages())
                .data(savedJobResponseList)
                .last(savedJobs.isLast())
                .build();
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
