package com.recruitment.job_service.service;

import com.recruitment.job_service.dto.request.JobCreationRequest;
import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.dto.response.PageResponse;
import com.recruitment.job_service.entity.Job;
import com.recruitment.job_service.mapper.JobMapper;
import com.recruitment.job_service.repository.JobRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobService {
    JobRepository jobRepository;
    JobMapper jobMapper;

   public JobResponse createJob(JobCreationRequest jobCreationRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Job job = Job.builder()
                .userId(authentication.getName())
                .content(jobCreationRequest.getContent())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();

        var user =  jobRepository.save(job);
        return  jobMapper.entityToJobResponse(user);
   }

   public PageResponse<JobResponse> getAllJobPosts(int page, int size, String sortField, String sortDirection) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userId =  authentication.getName();
       Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
               Sort.by(sortField).ascending() : Sort.by(sortField).descending();

       Pageable pageable = PageRequest.of(page, size, sort);
       var pageData = jobRepository.findAllByUserId(userId,pageable);

       return PageResponse.<JobResponse>builder()
               .pageNo(page)
               .pageSize(size)
               .totalPages(pageData.getTotalPages())
               .totalElements(pageData.getTotalElements())
               .data(pageData.getContent().stream().map(jobMapper::entityToJobResponse).toList())
               .build();
   }
}
