package com.recruitment.api_gateway.service;

import com.recruitment.api_gateway.IdentityClient;
import com.recruitment.api_gateway.dto.request.IntrospectRequest;
import com.recruitment.api_gateway.dto.response.ApiResponse;
import com.recruitment.api_gateway.dto.response.IntrospectResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token){
        return identityClient.introspect(IntrospectRequest.builder()
                .token(token)
                .build());
    }
}
