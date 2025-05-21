package com.recruitment.job_service.service;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.job_service.dto.request.JobCreationRequest;
import com.recruitment.job_service.dto.response.JobResponse;
import com.recruitment.job_service.dto.response.UserProfileResponse;
import com.recruitment.job_service.entity.*;
import com.recruitment.job_service.mapper.JobMapper;
import com.recruitment.job_service.repository.*;
import com.recruitment.job_service.repository.httpclient.ProfileFeignRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobService {
    JobRepository jobRepository;
    JobMapper jobMapper;
    IndustryRepository industryRepo;
    JobLevelRepository jobLevelRepo;
    ExperienceLevelRepository experienceLevelRepo;
    SalaryRangeRepository salaryRangeRepo;
    WorkTypeRepository workTypeRepo;
    ProfileFeignRepository profileFeignRepository;

    @Transactional
   public JobResponse createJob(JobCreationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Lấy các entity
        Map<UUID, Object> entities = fetchEntitiesByIds(Arrays.asList(
                request.getIndustryId(), request.getJobLevelId(), request.getExperienceLevelId(),
                request.getSalaryRangeId(), request.getWorkTypeId()
        ));

        List<String> errors = new ArrayList<>();
        Industry industry = (Industry) entities.get(request.getIndustryId());
        if (industry == null) errors.add("Industry not found");
        JobLevel jobLevel = (JobLevel) entities.get(request.getJobLevelId());
        if (jobLevel == null) errors.add("Job level not found");
        ExperienceLevel expLevel = (ExperienceLevel) entities.get(request.getExperienceLevelId());
        if (expLevel == null) errors.add("Experience level not found");
        SalaryRange salaryRange = (SalaryRange) entities.get(request.getSalaryRangeId());
        if (salaryRange == null) errors.add("Salary range not found");
        WorkType workType = (WorkType) entities.get(request.getWorkTypeId());
        if (workType == null) errors.add("Work type not found");

        if (!errors.isEmpty()) {
            log.error("Validation errors: {}", String.join("; ", errors));
            throw new IllegalArgumentException(String.join("; ", errors));
        }
        Job job = jobMapper.toEntity(request);
        job.setEmployerId(UUID.fromString(authentication.getName()));
        job.setIndustry(industry);
        job.setJobLevel(jobLevel);
        job.setExperienceLevel(expLevel);
        job.setSalaryRange(salaryRange);
        job.setWorkType(workType);
        job.setStatus(Job.JobPostStatus.PENDING);
        return  jobMapper.toResponse(jobRepository.save(job));
   }

    @Cacheable(value = "entitiesCache", key = "#ids")
    public Map<UUID, Object> fetchEntitiesByIds(List<UUID> ids) {
        // Khởi tạo map để lưu trữ kết quả
        Map<UUID, Object> result = new HashMap<>();

        // Lấy tất cả entity từ các repository
        List<Industry> industries = industryRepo.findAllById(ids);
        List<JobLevel> jobLevels = jobLevelRepo.findAllById(ids);
        List<ExperienceLevel> experienceLevels = experienceLevelRepo.findAllById(ids);
        List<SalaryRange> salaryRanges = salaryRangeRepo.findAllById(ids);
        List<WorkType> workTypes = workTypeRepo.findAllById(ids);

        // Gộp tất cả entity vào map
        industries.forEach(entity -> result.put(entity.getId(), entity));
        jobLevels.forEach(entity -> result.put(entity.getId(), entity));
        experienceLevels.forEach(entity -> result.put(entity.getId(), entity));
        salaryRanges.forEach(entity -> result.put(entity.getId(), entity));
        workTypes.forEach(entity -> result.put(entity.getId(), entity));

        log.debug("Fetched {} entities for IDs: {}", result.size(), ids);
        return result;
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

       var pageData = jobRepository.findAllByEmployerId(UUID.fromString(employerId),pageable);
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
