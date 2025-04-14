package com.recruitment.identity.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                Object principal = SecurityContextHolder.getContext().getAuthentication();
                if (principal instanceof JwtAuthenticationToken token) {
                    String jwtToken = token.getToken().getTokenValue();
                    template.header("Authorization", "Bearer " + jwtToken);
                }
            }
        };
    }
}
