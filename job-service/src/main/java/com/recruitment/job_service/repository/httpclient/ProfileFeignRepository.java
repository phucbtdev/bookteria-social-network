package com.recruitment.job_service.repository.httpclient;

import com.recruitment.job_service.dto.response.ApiResponse;
import com.recruitment.job_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "profile-service",
        url = "http://localhost:8081/profile"
)
public interface ProfileFeignRepository {

    @GetMapping(value = "/internal/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserProfileResponse  getUserProfile(@PathVariable String userId);
}
