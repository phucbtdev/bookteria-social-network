package com.recruitment.candidate_service.service;

import com.recruitment.candidate_service.dto.request.CandidatePackageCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidatePackageUpdateRequest;
import com.recruitment.candidate_service.dto.response.CandidatePackageResponse;
import com.recruitment.candidate_service.entity.CandidatePackage;
import com.recruitment.candidate_service.exception.AppException;
import com.recruitment.candidate_service.exception.ErrorCode;
import com.recruitment.candidate_service.mapper.CandidatePackageMapper;
import com.recruitment.candidate_service.repository.CandidatePackageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
        return candidatePackageMapper.toCandidatePackageResponse(candidatePackageRepository.save(candidatePackage));
    }

    public CandidatePackageResponse updatePackage(Integer id, CandidatePackageUpdateRequest request) {
        CandidatePackage candidatePackage = candidatePackageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        candidatePackageMapper.updateCandidatePackageFromRequest(candidatePackage, request);
        return candidatePackageMapper.toCandidatePackageResponse(candidatePackage);
    }

    public List<CandidatePackageResponse> getAllPackages() {
        List<CandidatePackage> candidatePackages = candidatePackageRepository.findAll();
        return candidatePackages.stream()
                .map(candidatePackageMapper::toCandidatePackageResponse)
                .toList();
    }

    public CandidatePackageResponse getPackageById(Integer id) {
        CandidatePackage candidatePackage = candidatePackageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        return candidatePackageMapper.toCandidatePackageResponse(candidatePackage);
    }

    public void deletePackage(Integer id) {
        CandidatePackage candidatePackage = candidatePackageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        candidatePackageRepository.delete(candidatePackage);
    }
}
