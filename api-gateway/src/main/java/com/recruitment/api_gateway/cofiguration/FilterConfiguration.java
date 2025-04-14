package com.recruitment.api_gateway.cofiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.api_gateway.dto.response.ApiResponse;
import com.recruitment.api_gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class FilterConfiguration implements GlobalFilter, Ordered {
    ObjectMapper objectMapper;
    IdentityService identityService;
    private static final int UNAUTHENTICATED_CODE = 1401;
    private static final String UNAUTHENTICATED_MESSAGE = "Unauthenticated";

    @Value("${app.api-prefix}")
    @NonFinal
    String apiPrefix;

    @NonFinal
    private String[] publicEndpoints = {
            "/identity/auth/.*",
            "/identity/users/register"
    };
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("Processing request for path: {}", path);

        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);

        //Check null & empty bearer token
        List<String> authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || CollectionUtils.isEmpty(authHeader)) {
            log.warn("Missing Authorization header for path: {}", path);
            return unauthenticated(exchange.getResponse());
        }

        //Check type bearerToken
        String bearerToken = authHeader.stream()
                .filter(header -> header != null && header.startsWith("Bearer "))
                .findFirst()
                .orElse(null);
        if (bearerToken == null) {
            log.warn("Bearer token missing or malformed in Authorization header for path: {}", path);
            return unauthenticated(exchange.getResponse());
        }

        String token = bearerToken.replace("Bearer ", "");
        log.info("Authenticating token for path: {}", path);

        return identityService.introspect(token).flatMap(introspectResponse -> {
            boolean isValid = introspectResponse != null
                    && introspectResponse.getResult() != null
                    && introspectResponse.getResult().isValid();
            if (isValid) {
                log.info("Token is valid for path: {}", path);
                return chain.filter(exchange);
            }
            else {
                log.warn("Token introspection failed or token is invalid for path: {}", path);
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> {
            log.error("Error during token introspection for path {}: {}", path, throwable.getMessage(), throwable);
            return unauthenticated(exchange.getResponse());
        });
    }
    private boolean isPublicEndpoint(ServerHttpRequest request){
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    /**
     * Xử lý phản hồi 401 Unauthorized một cách an toàn trong môi trường reactive.
     */
    Mono<Void> unauthenticated(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON); // Đảm bảo Content-Type

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(UNAUTHENTICATED_CODE)
                .message(UNAUTHENTICATED_MESSAGE)
                .build();

        return Mono.fromCallable(() -> objectMapper.writeValueAsString(apiResponse))
                .map(body -> response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8)))
                .flatMap(buffer -> response.writeWith(Mono.just(buffer)))
                .doOnError(e -> log.error("Error processing unauthenticated response", e))
                .onErrorResume(e -> {
                    log.error("Failed to send unauthenticated response, completing.", e);
                     response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return response.setComplete();
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
