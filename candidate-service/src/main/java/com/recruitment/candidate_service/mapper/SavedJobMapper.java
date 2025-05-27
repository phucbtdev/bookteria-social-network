package com.recruitment.candidate_service.mapper;

import com.recruitment.candidate_service.dto.request.SavedJobCreationRequest;
import com.recruitment.candidate_service.dto.response.SavedJobResponse;
import com.recruitment.candidate_service.entity.SavedJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SavedJobMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "candidate", ignore = true)
    @Mapping(target = "savedAt", ignore = true)
    SavedJob toEntity(SavedJobCreationRequest request);

    @Mapping(target = "candidateId", source = "candidate.id")
    SavedJobResponse toResponse(SavedJob savedJob);

}
