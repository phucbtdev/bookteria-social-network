package com.recruitment.employer_service.mapper;

import com.recruitment.employer_service.dto.request.EmployerPackageCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerPackageUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerPackageResponse;
import com.recruitment.employer_service.entity.EmployerPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployerPackageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerPackage toEntity(EmployerPackageCreationRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerPackage toEntity(EmployerPackageUpdateRequest request);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerPackageResponse toResponse(EmployerPackage employerPackage);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget EmployerPackage employerPackage, EmployerPackageUpdateRequest request);
}
