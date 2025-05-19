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
import com.recruitment.common.dto.response.PageResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        //Mapping request to entity
        Candidate candidate = candidateMapper.toCandidate(creationRequest);

        //Finding candidate by name
        CandidatePackage candidatePackage = candidatePackageRepository
                .findByName("Basic");
        if(candidatePackage == null) throw new AppException(ErrorCode.RECORD_NOT_EXISTED);

        //Save candidate
        Candidate savedCandidate = candidateRepository.save(candidate);

        //Setting candidate package subscription
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(candidatePackage.getDurationDays());
        CandidatePackageSubscription subscription = CandidatePackageSubscription.builder()
                .candidate(savedCandidate)
                .candidatePackage(candidatePackage)
                .startDate(now)
                .endDate(endDate)
                .amountPaid(BigDecimal.ZERO)
                .jobApplicationsUsed(0)
                .status(CandidatePackageSubscription.SubscriptionStatus.ACTIVE)
                .build();

        //Save subscription
        CandidatePackageSubscription savedsubscription = candidatePackageSubscriptionRepository.save(subscription);

        //Update the employer with the subscription reference
        savedCandidate.setSubscription(savedsubscription);
        savedCandidate.setPackageExpiryDate(endDate);

        //Save the candidate again with the subscription
        candidateRepository.save(savedCandidate);

        //Publish the event
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
        return candidateMapper.toResponse(candidate);
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
        return candidateMapper.toResponse(candidate);
    }

    public PageResponse<CandidateResponse> getCandidateList(
            int page,
            int size,
            String sortBy,
            String sortDir

    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Candidate> pageData = candidateRepository.findAll(pageable);
        List<CandidateResponse> dataList = pageData.getContent().stream()
                .map(candidateMapper::toResponse)
                .toList();


        return PageResponse.<CandidateResponse>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(dataList)
                .build();

    }

    public CandidateResponse getCandidateById(
            UUID id
    ) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        return candidateMapper.toResponse(candidate);
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
