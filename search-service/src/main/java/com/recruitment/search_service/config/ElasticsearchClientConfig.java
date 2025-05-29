package com.recruitment.search_service.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchClientConfig {
    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.protocol}")
    private String protocol;

    private final ElasticsearchClient elasticsearchClient;

    @Bean
    public ObjectMapper elasticsearchObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Hỗ trợ LocalDateTime
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ObjectMapper objectMapper) {
        RestClient restClient = RestClient.builder(
                new HttpHost(host, port, protocol)
        ).build();

        // Tạo transport với JacksonJsonpMapper để sử dụng ObjectMapper tùy chỉnh
        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper(objectMapper)
        );

        // Tạo và trả về ElasticsearchClient
        return new ElasticsearchClient(transport);
    }

    @PostConstruct
    public void initializeIndex() throws IOException {
        // Kiểm tra xem index đã tồn tại chưa
        boolean indexExists = elasticsearchClient.indices()
                .exists(e -> e.index("jobs"))
                .value();

        if (!indexExists) {
            CreateIndexRequest createIndexRequest = CreateIndexRequest.of(i -> i
                    .index("jobs")
                    .mappings(m -> m
                            .properties("title", p -> p.text(t -> t))
                            .properties("slug", p -> p.keyword(k -> k))
                            .properties("description", p -> p.text(t -> t))
                            .properties("industryId", p -> p.keyword(k -> k))
                            .properties("jobLevelId", p -> p.keyword(k -> k))
                            .properties("experienceLevelId", p -> p.keyword(k -> k))
                            .properties("salaryRangeId", p -> p.keyword(k -> k))
                            .properties("workTypeId", p -> p.keyword(k -> k))
                            .properties("skillsRequired", p -> p.text(t -> t))
                            .properties("genderRequirement", p -> p.keyword(k -> k))
                            .properties("address", p -> p.text(t -> t))
                            .properties("latitude", p -> p.geoPoint(g -> g))
                            .properties("longitude", p -> p.geoPoint(g -> g))
                            .properties("applicationDeadline", p -> p.date(d -> d))
                            .properties("status", p -> p.keyword(k -> k))
                    )
            );
            elasticsearchClient.indices().create(createIndexRequest);
        }
    }
}