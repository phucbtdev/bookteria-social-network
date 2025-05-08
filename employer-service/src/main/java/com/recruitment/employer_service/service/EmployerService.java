package com.recruitment.employer_service.service;

import com.recruitment.common.dto.request.EmployerCreationRequest;
import com.recruitment.employer_service.entity.EmployerPackage;
import com.recruitment.employer_service.entity.EmployerPackageSubscriptions;
import com.recruitment.employer_service.event.EmployerCreatedEvent;
import com.recruitment.employer_service.exception.AppException;
import com.recruitment.employer_service.exception.ErrorCode;
import com.recruitment.employer_service.repository.EmployerPackageRepository;
import com.recruitment.employer_service.repository.EmployerPackageSubscriptionRepository;
import com.recruitment.employer_service.repository.EmployerRepository;
import com.recruitment.employer_service.dto.request.EmployerUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerResponse;
import com.recruitment.employer_service.entity.Employer;
import com.recruitment.employer_service.mapper.EmployerMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE ,makeFinal = true)
public class EmployerService {
    EmployerRepository employerRepository;
    EmployerMapper employerMapper;
    EmployerPackageSubscriptionRepository employerPackageSubscriptionRepository;
    EmployerPackageRepository employerPackageRepository;
    ApplicationEventPublisher applicationEventPublisher;

    public EmployerResponse createEmployer(EmployerCreationRequest request) {
        return employerMapper.toEmployerResponse(employerRepository.save(employerMapper.toEmployer(request)));
    }

    @Transactional
    public void createEmployerFromIdentity(EmployerCreationRequest creationRequest){
        Employer employer = employerMapper.toEmployer(creationRequest);
        Employer savedEmployer = employerRepository.save(employer);

        EmployerPackage employerPackage = employerPackageRepository
                .findById(creationRequest.getCurrentPackageId())
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));

        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusMonths(1);

        EmployerPackageSubscriptions employerPackageSubscriptions = EmployerPackageSubscriptions.builder()
                .employer(savedEmployer)
                .employerPackage(employerPackage)
                .startDate(now)
                .endDate(endDate)
                .isActive(true)
                .status("ACTIVE")
                .build();
        employerPackageSubscriptionRepository.save(employerPackageSubscriptions);

        applicationEventPublisher.publishEvent(
                new EmployerCreatedEvent(
                        savedEmployer.getId(),
                        savedEmployer.getUserId(),
                        savedEmployer.getCompanyName()
                )
        );
    }


    public EmployerResponse updateEmployer(UUID id, EmployerUpdateRequest request) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        employerMapper.updateEmployerFromRequest(employer, request);
        return employerMapper.toEmployerResponse(employerRepository.save(employer));
    }

    public List<EmployerResponse> getAllEmployers() {
        return employerRepository.findAll()
                .stream()
                .map(employerMapper::toEmployerResponse)
                .toList();
    }

    public EmployerResponse getEmployerById(UUID id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        return employerMapper.toEmployerResponse(employer);
    }

    public void deleteEmployer(UUID id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        employerRepository.delete(employer);
    }
}
