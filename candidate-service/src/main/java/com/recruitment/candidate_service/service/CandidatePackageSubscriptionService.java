package com.recruitment.candidate_service.service;

import com.recruitment.candidate_service.dto.request.CandidatePackageSubscriptionCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidatePackageSubscriptionUpdateRequest;
import com.recruitment.candidate_service.dto.response.ApiResponse;
import com.recruitment.candidate_service.dto.response.CandidatePackageSubscriptionResponse;
import com.recruitment.candidate_service.entity.CandidatePackageSubscription;
import com.recruitment.candidate_service.exception.AppException;
import com.recruitment.candidate_service.exception.ErrorCode;
import com.recruitment.candidate_service.mapper.CandidatePackageSubscriptionMapper;
import com.recruitment.candidate_service.repository.CandidatePackageSubscriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CandidatePackageSubscriptionService {
    CandidatePackageSubscriptionMapper candidatePackageSubscriptionMapper;
    CandidatePackageSubscriptionRepository candidatePackageSubscriptionRepository;

    public CandidatePackageSubscriptionResponse createCandidatePackageSubscription(
            @RequestBody CandidatePackageSubscriptionCreationRequest request
    ) {
        return candidatePackageSubscriptionMapper.toResponse(
                candidatePackageSubscriptionRepository.save(
                        candidatePackageSubscriptionMapper.toEntity(request)
                )
        );
    }

    public CandidatePackageSubscriptionResponse updateCandidatePackageSubscription(
            @PathVariable UUID id,
            @RequestBody CandidatePackageSubscriptionUpdateRequest request
    ) {
        CandidatePackageSubscription candidatePackageSubscription = candidatePackageSubscriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        candidatePackageSubscriptionMapper.updateEntity(candidatePackageSubscription, request);

        return candidatePackageSubscriptionMapper.toResponse(
                candidatePackageSubscriptionRepository.save(candidatePackageSubscription)
        );
    }

    public List<CandidatePackageSubscriptionResponse> getAllCandidatePackageSubscriptions() {
        return candidatePackageSubscriptionRepository.findAll().stream()
                .map(candidatePackageSubscriptionMapper::toResponse)
                .toList();
    }

    public CandidatePackageSubscriptionResponse getCandidatePackageSubscription(
            @PathVariable UUID id
    ) {
        CandidatePackageSubscription candidatePackageSubscription = candidatePackageSubscriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));

        return candidatePackageSubscriptionMapper.toResponse(candidatePackageSubscription);
    }

    public void deleteCandidatePackageSubscription(
            @PathVariable UUID id
    ) {
        CandidatePackageSubscription candidatePackageSubscription = candidatePackageSubscriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));

        candidatePackageSubscriptionRepository.delete(candidatePackageSubscription);
    }

}
