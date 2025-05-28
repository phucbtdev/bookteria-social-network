package com.recruitment.search_service.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.Duration;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.recruitment.search_service.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.host:localhost}")
    private String host;

    @Value("${elasticsearch.port:9200}")
    private int port;

    @Value("${elasticsearch.protocol:http}")
    private String protocol;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // Tạo RestClient với thông tin host, port và protocol
        RestClient restClient = RestClient.builder(
                new HttpHost(host, port, protocol)
        ).build();

        // Cấu hình ObjectMapper để xử lý JSON, hỗ trợ các kiểu dữ liệu Java Time
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Tạo RestClientTransport để kết nối RestClient với JSON mapper
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper(objectMapper)
        );

        // Trả về ElasticsearchClient
        return new ElasticsearchClient(transport);
    }

    @Override
    public ClientConfiguration clientConfiguration() {
        // Cấu hình ClientConfiguration cho Spring Data Elasticsearch
        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .usingSsl(String.valueOf(protocol.equalsIgnoreCase("https")))
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(3))
                .build();
    }
}