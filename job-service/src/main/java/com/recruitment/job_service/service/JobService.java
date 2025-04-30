package com.recruitment.job_service.service;

import com.recruitment.job_service.dto.request.JobCreationRequest;
import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.dto.response.PageResponse;
import com.recruitment.job_service.dto.response.UserProfileResponse;
import com.recruitment.job_service.entity.Job;
import com.recruitment.job_service.mapper.JobMapper;
import com.recruitment.job_service.repository.JobRepository;
import com.recruitment.job_service.repository.httpclient.ProfileFeignRepository;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobService {
    JobRepository jobRepository;
    JobMapper jobMapper;
    ProfileFeignRepository profileFeignRepository;

   public JobResponse createJob(JobCreationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Job job = jobMapper.toEntity(request);
        job.setEmployerId(UUID.fromString(authentication.getName()));
        return  jobMapper.toResponse(jobRepository.save(job));
   }

   public PageResponse<JobResponse> getAllJobPosts(int page, int size, String sortField, String sortDirection) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String employerId =  authentication.getName();

       Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
               Sort.by(sortField).ascending() : Sort.by(sortField).descending();
       Pageable pageable = PageRequest.of(page, size, sort);

       UserProfileResponse userProfileResponse =  null;
       try {
           userProfileResponse  = profileFeignRepository.getUserProfile(employerId);
       } catch (Exception e) {
           log.error("Error fetching user profile: {}", e.getMessage());
       }

       String username = userProfileResponse != null ? userProfileResponse.getFirstName() : null;

       var pageData = jobRepository.findAllByUserId(employerId,pageable);
       var postList = pageData.getContent().stream().map(job -> {
           JobResponse jobResponse = jobMapper.toResponse(job);
//           jobResponse.setUsername(username);
           return jobResponse;
       }).toList();

       return PageResponse.<JobResponse>builder()
               .pageNo(page)
               .pageSize(size)
               .totalPages(pageData.getTotalPages())
               .totalElements(pageData.getTotalElements())
               .data(postList)
               .build();
   }
}
