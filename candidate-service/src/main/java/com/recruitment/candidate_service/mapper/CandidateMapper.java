package com.recruitment.candidate_service.mapper;

import com.recruitment.candidate_service.dto.request.CandidateCreationRequest;
import com.recruitment.candidate_service.dto.request.CandidateUpdateRequest;
import com.recruitment.candidate_service.dto.response.CandidateResponse;
import com.recruitment.candidate_service.entity.Candidate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    @Mapping(target = "id", ignore = true)
    Candidate toCandidate(CandidateCreationRequest candidateCreationRequest);
    CandidateResponse toCandidateResponse(Candidate candidate);

    @Mapping(target = "userId", ignore = true)
    void updateCandidateFromRequest(@MappingTarget Candidate candidate, CandidateUpdateRequest request);
}
