package com.recruitment.employer_service.service;

import com.recruitment.employer_service.exception.AppException;
import com.recruitment.employer_service.exception.ErrorCode;
import com.recruitment.employer_service.repository.EmployerRepository;
import com.recruitment.employer_service.dto.request.EmployerUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerResponse;
import com.recruitment.employer_service.entity.Employer;
import com.recruitment.employer_service.mapper.EmployerMapper;
import com.recruitment.event.dto.EmployerCreationRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE ,makeFinal = true)
public class EmployerService {
    EmployerRepository employerRepository;
    EmployerMapper employerMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public EmployerResponse createEmployer(EmployerCreationRequest request) {
        return employerMapper.toEmployerResponse(employerRepository.save(employerMapper.toEmployer(request)));
    }

    @KafkaListener(topics = "employer-registration")
    public void createEmployerFromIdentity(EmployerCreationRequest creationRequest){
        log.info("Received user creation event: {}", creationRequest);
        try {
            Employer employer = employerMapper.toEmployer(creationRequest);
            employerRepository.save(employer);
            kafkaTemplate.send("employer-creation-success", Map.of(
                            "userId", creationRequest.getUserId().toString(),
                            "employerId", employer.getId().toString(),
                            "companyName", employer.getCompanyName()
                    ));
        } catch (Exception e) {
            kafkaTemplate.send("employer-creation-failed", Map.of(
                    "userId", creationRequest.getUserId(),
                    "reason", "Exception: " + e.getMessage()
            ) );
        }
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
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        return employerMapper.toEmployerResponse(employer);
    }

    public void deleteEmployer(UUID id) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        employerRepository.delete(employer);
    }
}
