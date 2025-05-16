package com.recruitment.candidate_service.service;

import com.recruitment.candidate_service.dto.request.CandidatePackageSubscriptionCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidatePackageSubscriptionUpdateRequest;
import com.recruitment.candidate_service.dto.response.CandidatePackageSubscriptionResponse;
import com.recruitment.candidate_service.entity.CandidatePackageSubscription;
import com.recruitment.candidate_service.exception.AppException;
import com.recruitment.candidate_service.exception.ErrorCode;
import com.recruitment.candidate_service.mapper.CandidatePackageSubscriptionMapper;
import com.recruitment.candidate_service.repository.CandidatePackageSubscriptionRepository;
import com.recruitment.common.dto.response.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PageResponse<CandidatePackageSubscriptionResponse> getAllCandidatePackageSubscriptions(
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {


        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<CandidatePackageSubscription> pageData = candidatePackageSubscriptionRepository.findAll(pageable);
        List<CandidatePackageSubscriptionResponse> dataList = pageData.getContent().stream()
                .map(candidatePackageSubscriptionMapper::toResponse)
                .toList();

        return PageResponse.<CandidatePackageSubscriptionResponse>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .last(pageData.isLast())
                .data(dataList)
                .build();

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
