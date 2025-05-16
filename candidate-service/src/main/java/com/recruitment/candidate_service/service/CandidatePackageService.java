package com.recruitment.candidate_service.service;

import com.recruitment.candidate_service.dto.request.CandidatePackageCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidatePackageUpdateRequest;
import com.recruitment.candidate_service.dto.response.CandidatePackageResponse;
import com.recruitment.candidate_service.entity.CandidatePackage;
import com.recruitment.candidate_service.exception.AppException;
import com.recruitment.candidate_service.exception.ErrorCode;
import com.recruitment.candidate_service.mapper.CandidatePackageMapper;
import com.recruitment.candidate_service.repository.CandidatePackageRepository;
import com.recruitment.common.dto.response.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CandidatePackageService {
    CandidatePackageRepository candidatePackageRepository;
    CandidatePackageMapper candidatePackageMapper;

    public CandidatePackageResponse createPackage(CandidatePackageCreationRequest request) {
        CandidatePackage candidatePackage = candidatePackageMapper.toCandidatePackage(request);
        return candidatePackageMapper.toResponse(candidatePackageRepository.save(candidatePackage));
    }

    public CandidatePackageResponse updatePackage(
            Integer id,
            CandidatePackageUpdateRequest request
    ) {
        CandidatePackage candidatePackage = candidatePackageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        candidatePackageMapper.updateCandidatePackageFromRequest(candidatePackage, request);
        return candidatePackageMapper.toResponse(candidatePackage);
    }

    public PageResponse<CandidatePackageResponse> getAllPackages(
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<CandidatePackage> pageData = candidatePackageRepository.findAll(pageable);

        List<CandidatePackageResponse> dataList = pageData.getContent().stream()
                .map(candidatePackageMapper::toResponse)
                .toList();

        return PageResponse.<CandidatePackageResponse>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .last(pageData.isLast())
                .data(dataList)
                .build();

    }

    public CandidatePackageResponse getPackageById(Integer id) {
        CandidatePackage candidatePackage = candidatePackageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        return candidatePackageMapper.toResponse(candidatePackage);
    }

    public void deletePackage(Integer id) {
        CandidatePackage candidatePackage = candidatePackageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        candidatePackageRepository.delete(candidatePackage);
    }
}
