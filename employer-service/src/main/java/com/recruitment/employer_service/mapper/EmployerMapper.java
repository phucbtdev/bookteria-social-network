package com.recruitment.employer_service.mapper;

import com.recruitment.employer_service.dto.request.EmployerCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerResponse;
import com.recruitment.employer_service.entity.Employer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    Employer toEmployer(EmployerCreationRequest employerCreationRequest);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerResponse toEmployerResponse(Employer employer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateEmployerFromRequest(@MappingTarget Employer employer, EmployerUpdateRequest employerUpdateRequest );
}
