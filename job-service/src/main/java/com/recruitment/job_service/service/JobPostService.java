package com.recruitment.job_service.service;

import com.recruitment.job_service.dto.request.JobPostRequest;
import com.recruitment.job_service.dto.response.JobPostResponse;
import com.recruitment.job_service.dto.response.PageResponse;
import com.recruitment.job_service.entity.JobPost;
import com.recruitment.job_service.mapper.JobPostMapper;
import com.recruitment.job_service.repository.JobPostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobPostService {
    JobPostRepository jobPostRepository;
    JobPostMapper jobPostMapper;

   public JobPostResponse createJob(JobPostRequest jobPostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JobPost jobPost = JobPost.builder()
                .userId(authentication.getName())
                .content(jobPostRequest.getContent())
                .createdDate(Instant.now())
                .modifiedDate(Instant.now())
                .build();

        var user =  jobPostRepository.save(jobPost);
        return  jobPostMapper.toJobPostResponse(user);
   }

   public PageResponse<JobPostResponse> getAllJobPosts(int page, int size, String sortField, String sortDirection) {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       String userId =  authentication.getName();
       Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
               Sort.by(sortField).ascending() : Sort.by(sortField).descending();

       Pageable pageable = PageRequest.of(page, size, sort);
       var pageData = jobPostRepository.findAllByUserId(userId,pageable);

       return PageResponse.<JobPostResponse>builder()
               .pageNo(page)
               .pageSize(size)
               .totalPages(pageData.getTotalPages())
               .totalElements(pageData.getTotalElements())
               .data(pageData.getContent().stream().map(jobPostMapper::toJobPostResponse).toList())
               .build();
   }
}
