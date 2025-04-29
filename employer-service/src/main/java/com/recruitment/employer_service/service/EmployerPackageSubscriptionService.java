package com.recruitment.employer_service.service;

import com.recruitment.employer_service.dto.request.EmployerPackageSubscriptionCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerPackageSubscriptionUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerPackageSubscriptionResponse;
import com.recruitment.employer_service.entity.EmployerPackageSubscriptions;
import com.recruitment.employer_service.exception.AppException;
import com.recruitment.employer_service.exception.ErrorCode;
import com.recruitment.employer_service.mapper.EmployerPackageSubscriptionMapper;
import com.recruitment.employer_service.repository.EmployerPackageSubscriptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerPackageSubscriptionService {
    EmployerPackageSubscriptionRepository employerPackageSubscriptionRepository;
    EmployerPackageSubscriptionMapper employerPackageSubscriptionMapper;

    public EmployerPackageSubscriptionResponse createSubscription(EmployerPackageSubscriptionCreationRequest request) {
        return employerPackageSubscriptionMapper.toResponse(
                employerPackageSubscriptionRepository.save(
                        employerPackageSubscriptionMapper.toEntity(request)
                )
        );
    }

    public EmployerPackageSubscriptionResponse updateSubscription(UUID id, EmployerPackageSubscriptionUpdateRequest request) {
        EmployerPackageSubscriptions subscription = employerPackageSubscriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));

        employerPackageSubscriptionMapper.updateEntity(subscription,request);

        return employerPackageSubscriptionMapper.toResponse(
                employerPackageSubscriptionRepository.save(
                        subscription
                )
        );
    }

    public List<EmployerPackageSubscriptionResponse> getAllSubscriptions() {
        return employerPackageSubscriptionRepository.findAll()
                .stream()
                .map(employerPackageSubscriptionMapper::toResponse)
                .toList();
    }

    public EmployerPackageSubscriptionResponse getSubscriptionById(UUID id) {
        return employerPackageSubscriptionMapper.toResponse(
                employerPackageSubscriptionRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED))
        );
    }

    public void deleteSubscription(UUID id) {
        EmployerPackageSubscriptions subscription = employerPackageSubscriptionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));

        employerPackageSubscriptionRepository.delete(subscription);
    }
}
