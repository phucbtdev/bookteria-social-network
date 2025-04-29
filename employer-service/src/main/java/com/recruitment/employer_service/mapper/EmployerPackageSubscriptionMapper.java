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
    EmployerPackageSubscriptions toEntity(EmployerPackageSubscriptionCreationRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employerId", target = "employer.id")
    @Mapping(source = "packageId", target = "employerPackage.id")
    EmployerPackageSubscriptions toEntity(EmployerPackageSubscriptionUpdateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "employer.id", target = "employerId")
    @Mapping(source = "employerPackage.id", target = "packageId")
    EmployerPackageSubscriptionResponse toResponse(EmployerPackageSubscriptions subscription);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "employerId", target = "employer.id")
    @Mapping(source = "packageId", target = "employerPackage.id")
    void updateEntity(@MappingTarget EmployerPackageSubscriptions subscription, EmployerPackageSubscriptionUpdateRequest request);
}
