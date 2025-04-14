package com.recruitment.api_gateway.cofiguration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "gateway.auth")
@Data
public class AuthProperties {
    private String apiPrefix = "/api/v1";
    private SkipPaths skip = new SkipPaths();

    @Data
    public static class SkipPaths {
        private List<String> prefixes = new ArrayList<>();
    }
}
