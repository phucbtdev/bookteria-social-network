package com.recruitment.employer_service.service;

import com.recruitment.employer_service.dto.request.EmployerPackageCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerPackageUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerPackageResponse;
import com.recruitment.employer_service.entity.EmployerPackage;
import com.recruitment.employer_service.mapper.EmployerPackageMapper;
import com.recruitment.employer_service.repository.EmployerPackageRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployerPackageService {

    EmployerPackageMapper employerPackageMapper;
    EmployerPackageRepository employerPackageRepository;

    public EmployerPackageResponse createEmployerPackage(EmployerPackageCreationRequest request) {
        return employerPackageMapper.toResponse(employerPackageRepository.save(employerPackageMapper.toEntity(request)));
    }

    public EmployerPackageResponse updateEmployerPackage(Integer id, EmployerPackageUpdateRequest request) {
        EmployerPackage employerPackage = employerPackageRepository.findById(id).orElseThrow(() -> new RuntimeException("Employer package not found"));
        employerPackageMapper.updateEntity(employerPackage, request);
        return employerPackageMapper.toResponse(employerPackageRepository.save(employerPackage));
    }

    public List<EmployerPackageResponse> getAllEmployerPackages() {
        return employerPackageRepository.findAll()
                .stream()
                .map(employerPackageMapper::toResponse)
                .toList();
    }

    public  EmployerPackageResponse getEmployerPackageById(Integer id) {
        return employerPackageMapper.toResponse(employerPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer package not found")));
    }

    public void deleteEmployerPackage(Integer id) {
        EmployerPackage employerPackage = employerPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer package not found"));
        employerPackageRepository.delete(employerPackage);
    }

}
