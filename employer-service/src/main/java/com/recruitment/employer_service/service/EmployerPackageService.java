package com.recruitment.employer_service.service;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.employer_service.dto.request.EmployerPackageCreationRequest;
import com.recruitment.employer_service.dto.request.EmployerPackageUpdateRequest;
import com.recruitment.employer_service.dto.response.EmployerPackageResponse;
import com.recruitment.employer_service.entity.EmployerPackage;
import com.recruitment.employer_service.exception.AppException;
import com.recruitment.employer_service.exception.ErrorCode;
import com.recruitment.employer_service.mapper.EmployerPackageMapper;
import com.recruitment.employer_service.repository.EmployerPackageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE ,makeFinal = true)
public class EmployerPackageService {

    EmployerPackageMapper employerPackageMapper;
    EmployerPackageRepository employerPackageRepository;

    public EmployerPackageResponse createEmployerPackage(EmployerPackageCreationRequest request) {
        return employerPackageMapper.toResponse(employerPackageRepository.save(employerPackageMapper.toEntity(request)));
    }

    public EmployerPackageResponse updateEmployerPackage(Integer id, EmployerPackageUpdateRequest request) {
        EmployerPackage employerPackage = employerPackageRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RECORD_NOT_EXISTED));
        employerPackageMapper.updateEntity(employerPackage, request);
        return employerPackageMapper.toResponse(employerPackageRepository.save(employerPackage));
    }

    public PageResponse<EmployerPackageResponse> getAllEmployerPackages(
            int page,
            int size,
            String sortBy,
            String sortDir

    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<EmployerPackage> pageData = employerPackageRepository.findAll(pageable);
        List<EmployerPackageResponse> dataList = pageData.getContent().stream()
                .map(employerPackageMapper::toResponse)
                .toList();

        return PageResponse.<EmployerPackageResponse>builder()
                .pageNo(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .last(pageData.isLast())
                .data(dataList)
                .build();

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
