package com.recruitment.candidate_service.mapper;

import com.recruitment.candidate_service.dto.request.CandidatePackageCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidatePackageUpdateRequest;
import com.recruitment.candidate_service.dto.response.CandidatePackageResponse;
import com.recruitment.candidate_service.entity.CandidatePackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CandidatePackageMapper {
    @Mapping(target = "id", ignore = true)
    CandidatePackage toCandidatePackage(CandidatePackageCreationRequest request);

    CandidatePackageResponse toCandidatePackageResponse(CandidatePackage candidatePackage);

    @Mapping(target = "id", ignore = true)
    void updateCandidatePackageFromRequest(@MappingTarget CandidatePackage candidatePackage, CandidatePackageUpdateRequest request);
}
