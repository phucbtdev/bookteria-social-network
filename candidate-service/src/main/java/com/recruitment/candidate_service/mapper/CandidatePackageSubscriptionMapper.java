package com.recruitment.candidate_service.mapper;

import com.recruitment.candidate_service.dto.request.CandidatePackageSubscriptionCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidatePackageSubscriptionUpdateRequest;
import com.recruitment.candidate_service.dto.response.CandidatePackageSubscriptionResponse;
import com.recruitment.candidate_service.entity.CandidatePackageSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CandidatePackageSubscriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(source = "candidateId", target = "candidate.id")
    @Mapping(source = "packageId", target = "candidatePackage.id")
    CandidatePackageSubscription toEntity(CandidatePackageSubscriptionCreationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "candidateId", target = "candidate.id")
    @Mapping(source = "packageId", target = "candidatePackage.id")
    CandidatePackageSubscription toEntity(CandidatePackageSubscriptionUpdateRequest request);

    @Mapping(source = "candidate.id", target = "candidateId")
    @Mapping(source = "candidatePackage.id", target = "packageId")
    CandidatePackageSubscriptionResponse toResponse(CandidatePackageSubscription subscription);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "candidateId", target = "candidate.id")
    @Mapping(source = "packageId", target = "candidatePackage.id")
    void updateEntity(@MappingTarget CandidatePackageSubscription subscription, CandidatePackageSubscriptionUpdateRequest request);

}
