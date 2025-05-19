package com.recruitment.employer_service.mapper;

import com.recruitment.employer_service.dto.request.EmployerPackageSubscriptionCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerPackageSubscriptionUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerPackageSubscriptionResponse;
import com.recruitment.employer_service.entity.EmployerPackageSubscriptions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployerPackageSubscriptionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employerId", target = "employer.id")
    @Mapping(source = "packageId", target = "employerPackage.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerPackageSubscriptions toEntity(EmployerPackageSubscriptionCreationRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employerId", target = "employer.id")
    @Mapping(source = "packageId", target = "employerPackage.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployerPackageSubscriptions toEntity(EmployerPackageSubscriptionUpdateRequest request);

    @Mapping(source = "employer.id", target = "employerId")
    @Mapping(source = "employerPackage.id", target = "packageId")
    @Mapping(target = "createdAt", ignore = true)
    EmployerPackageSubscriptionResponse toResponse(EmployerPackageSubscriptions subscription);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employerId", target = "employer.id")
    @Mapping(source = "packageId", target = "employerPackage.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget EmployerPackageSubscriptions subscription, EmployerPackageSubscriptionUpdateRequest request);
}
