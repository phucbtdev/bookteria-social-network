package com.recruitment.identity.repository.httpclient;

import com.recruitment.identity.dto.request.ProfileCreationRequest;
import com.recruitment.identity.dto.response.ProfileCreationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "http://localhost:8081/profile")
public interface ProfileClient {
    @PostMapping(value = "/internal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ProfileCreationResponse createProfile(@RequestBody ProfileCreationRequest request);
}
