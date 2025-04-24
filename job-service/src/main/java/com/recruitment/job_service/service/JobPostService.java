package com.recruitment.job_service.service;

import com.recruitment.job_service.dto.request.JobPostRequest;
import com.recruitment.job_service.dto.response.JobPostResponse;
import com.recruitment.job_service.entity.JobPost;
import com.recruitment.job_service.mapper.JobPostMapper;
import com.recruitment.job_service.repository.JobPostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

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

   public List<JobPostResponse> getAllJobPosts() {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String userId =  authentication.getName();
       return jobPostRepository.findAllByUserId(userId).stream().map(jobPostMapper::toJobPostResponse).toList();

   }
}
