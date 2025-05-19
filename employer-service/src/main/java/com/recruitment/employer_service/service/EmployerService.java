package com.recruitment.employer_service.service;

import com.recruitment.common.dto.request.EmployerCreationRequest;
import com.recruitment.common.dto.response.PageResponse;
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
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
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
        return employerMapper.toResponse(employerRepository.save(employerMapper.toEmployer(request)));
    }

    public void createEmployerFromIdentity(EmployerCreationRequest creationRequest){
        //Create entity from request
        Employer employer = employerMapper.toEmployer(creationRequest);
        if (creationRequest.getSubscriptionId() != null) {
            EmployerPackageSubscriptions subscription = employerPackageSubscriptionRepository
                    .findById(creationRequest.getSubscriptionId())
                    .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));

            employer.setSubscription(subscription);
        }
        Employer savedEmployer =  employerRepository.save(employer);

        //Find the free package for the employer
        EmployerPackage employerPackage = employerPackageRepository.findByName("Basic");
        //Calculate subscription dates
        LocalDate now = LocalDate.now();
        LocalDate endDate = now.plusDays(employerPackage.getDurationDays());

        //Create package subscription with the saved employer that has ID
        EmployerPackageSubscriptions employerPackageSubscriptions = EmployerPackageSubscriptions.builder()
                .employer(savedEmployer)
                .employerPackage(employerPackage)
                .startDate(now)
                .endDate(endDate)
                .isActive(true)
                .status("ACTIVE")
                .build();

        log.info("employerPackageSubscriptions : {}", employerPackageSubscriptions);
        //Save the subscription to generate its ID
        EmployerPackageSubscriptions savedSubscription  =  employerPackageSubscriptionRepository.save(employerPackageSubscriptions);
            //Update the employer with the subscription reference
            savedEmployer.setSubscription(savedSubscription);
            savedEmployer.setPackageExpiryDate(endDate);

            employerRepository.save(savedEmployer);
            //Publish event about employer creation
            applicationEventPublisher.publishEvent(
                    new EmployerCreatedEvent(
                            savedEmployer.getId(),
                            savedEmployer.getUserId(),
                            savedEmployer.getCompanyName()
                    )
            );

            log.info("Employer created successfully: {}", savedEmployer);

    }


    public EmployerResponse updateEmployer(UUID id, EmployerUpdateRequest request) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        employerMapper.updateEmployerFromRequest(employer, request);
        return employerMapper.toResponse(employerRepository.save(employer));
    }

    public PageResponse<EmployerResponse> getAllEmployers(
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Employer> pageData = employerRepository.findAll(pageable);
        List<EmployerResponse> dataList = pageData.getContent().stream()
                .map(employerMapper::toResponse)
                .toList();

        return PageResponse.<EmployerResponse>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .last(pageData.isLast())
                .data(dataList)
                .build();

    }

    public EmployerResponse getEmployerById(UUID id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        return employerMapper.toResponse(employer);
    }

    public void deleteEmployer(UUID id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        employerRepository.delete(employer);
    }
}
