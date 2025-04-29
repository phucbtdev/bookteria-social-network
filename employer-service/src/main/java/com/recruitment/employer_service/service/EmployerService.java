package com.recruitment.employer_service.service;

import com.recruitment.employer_service.repository.EmployerRepository;
import com.recruitment.employer_service.dto.request.EmployerCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerResponse;
import com.recruitment.employer_service.entity.Employer;
import com.recruitment.employer_service.mapper.EmployerMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EmployerService {
    EmployerRepository employerRepository;
    EmployerMapper employerMapper;

    public EmployerResponse createEmployer(EmployerCreationRequest request) {
        return employerMapper.toEmployerResponse(employerRepository.save(employerMapper.toEmployer(request)));
    }

    public EmployerResponse updateEmployer(UUID id, EmployerUpdateRequest request) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
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
