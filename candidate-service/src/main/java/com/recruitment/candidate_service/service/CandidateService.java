package com.recruitment.candidate_service.service;

import com.recruitment.candidate_service.dto.request.CandidateUpdateRequest;
import com.recruitment.candidate_service.dto.response.CandidateResponse;
import com.recruitment.candidate_service.entity.Candidate;
import com.recruitment.candidate_service.entity.CandidatePackage;
import com.recruitment.candidate_service.entity.CandidatePackageSubscription;
import com.recruitment.candidate_service.event.CandidateCreatedEvent;
import com.recruitment.candidate_service.exception.AppException;
import com.recruitment.candidate_service.exception.ErrorCode;
import com.recruitment.candidate_service.mapper.CandidateMapper;
import com.recruitment.candidate_service.repository.CandidatePackageRepository;
import com.recruitment.candidate_service.repository.CandidatePackageSubscriptionRepository;
import com.recruitment.candidate_service.repository.CandidateRepository;
import com.recruitment.common.dto.request.CandidateCreationRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidateService {

    CandidateMapper candidateMapper;
    CandidateRepository candidateRepository;
    CandidatePackageRepository candidatePackageRepository;
    CandidatePackageSubscriptionRepository candidatePackageSubscriptionRepository;
    ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void createCandidateFromIdentity(
            CandidateCreationRequest creationRequest
    ){
        Candidate candidate = candidateMapper.toCandidate(creationRequest);
        Candidate savedCandidate = candidateRepository.save(candidate);

        CandidatePackage candidatePackage = candidatePackageRepository
                .findById(creationRequest.getCurrentPackageId())
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));

        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusMonths(1);

        CandidatePackageSubscription subscription = CandidatePackageSubscription.builder()
                .candidate(savedCandidate)
                .candidatePackage(candidatePackage)
                .startDate(now)
                .endDate(endDate)
                .amountPaid(BigDecimal.ZERO)
                .jobApplicationsUsed(0)
                .status(CandidatePackageSubscription.SubscriptionStatus.ACTIVE)
                .build();
        candidatePackageSubscriptionRepository.save(subscription);

        applicationEventPublisher.publishEvent(
                new CandidateCreatedEvent(
                        savedCandidate.getUserId(),
                        savedCandidate.getId()
                )
        );
    }

    public CandidateResponse createCandidate(
            CandidateCreationRequest request
    ){
        Candidate candidate = candidateMapper.toCandidate(request);
        log.info("Candidate created: {}", candidate.toString());
        candidate = candidateRepository.save(candidate);
        return candidateMapper.toCandidateResponse(candidate);
    }

    public CandidateResponse updateCandidate(
            UUID userId,
            CandidateUpdateRequest request
    ) {
        Candidate candidate = candidateRepository.findByUserId(userId);
        if (candidate == null) {
            throw new AppException(ErrorCode.RECORD_NOT_EXISTED);
        }
        candidateMapper.updateCandidateFromRequest(candidate,request);
        return candidateMapper.toCandidateResponse(candidate);
    }

    public List<CandidateResponse> getCandidateList() {
        return candidateRepository.findAll().stream()
                .map(candidateMapper::toCandidateResponse)
                .toList();
    }

    public CandidateResponse getCandidateById(
            UUID id
    ) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        return candidateMapper.toCandidateResponse(candidate);
    }


    public void deleteCandidate(
            UUID userId
    ) {
        Candidate candidate = candidateRepository.findByUserId(userId);
        if (candidate == null) {
            throw new AppException(ErrorCode.RECORD_NOT_EXISTED);
        }
        candidate.setDeleted(true);
        candidateRepository.save(candidate);
    }
}
