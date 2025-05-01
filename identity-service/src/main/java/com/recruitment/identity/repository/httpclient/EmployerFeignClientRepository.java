package com.recruitment.identity.repository.httpclient;


import com.recruitment.identity.configuration.FeignConfig;
import com.recruitment.identity.dto.ApiResponse;
import com.recruitment.identity.dto.request.EmployerCreationRequest;
import com.recruitment.identity.dto.response.EmployerCreationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "employer-service",
        url = "http://localhost:8085/employer-service",
        configuration = FeignConfig.class
)
public interface EmployerFeignClientRepository {
    @PostMapping(value = "/internal/employer", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<EmployerCreationResponse>  createEmployer(@RequestBody EmployerCreationRequest request);
}
