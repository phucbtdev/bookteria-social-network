package com.recruitment.search_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.search_service.document.JobDocument;
import com.recruitment.search_service.dto.HistorySearchDTO;
import com.recruitment.search_service.repository.JobSearchRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSearchService {

    private final JobSearchRepository jobRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final JwtDecoder jwtDecoder;

    private static final String TOPIC = "search-history-topic";

    public Page<JobDocument> searchJobs(String keyword, String location, String industry, int page, int size, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = getClientIp(request);
        String userId = extractUserIdFromJwt(request);

        // Tạo DTO lưu lịch sử tìm kiếm
        HistorySearchDTO historyDTO =  HistorySearchDTO.builder()
                .keyword(keyword)
                .location(location)
                .industry(industry)
                .userId(userId)
                .userAgent(userAgent)
                .ipAddress(ipAddress)
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .build();

        // Gửi event lịch sử tìm kiếm lên Kafka
        try {
            String jsonMessage = objectMapper.writeValueAsString(historyDTO);
            kafkaTemplate.send(TOPIC, jsonMessage);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // Cách 1: Sử dụng method đơn giản (khuyến nghị cho logic đơn giản)
        if (keyword != null && !keyword.isEmpty() &&
                location != null && !location.isEmpty() &&
                industry != null && !industry.isEmpty()) {
            return jobRepository.searchJobsWithAllFilters(keyword, location, industry, pageable);
        }

        // Cách 2: Xử lý từng trường hợp
        if (keyword != null && !keyword.isEmpty()) {
            if (location != null && !location.isEmpty()) {
                return jobRepository.findByTitleContainingOrDescriptionContainingOrSkillsRequiredContainingAndAddressContaining(
                        keyword, keyword, keyword, location, pageable);
            }
            return jobRepository.findByTitleContainingOrDescriptionContainingOrSkillsRequiredContaining(
                    keyword, keyword, keyword, pageable);
        }

        if (location != null && !location.isEmpty()) {
            return jobRepository.findByAddressContaining(location, pageable);
        }

        if (industry != null && !industry.isEmpty()) {
            return jobRepository.findByIndustryContaining(industry, pageable);
        }



        // Trả về tất cả nếu không có filter
        return jobRepository.findAll(pageable);
    }

    // Cách 3: Sử dụng method linh hoạt nhất
    public Page<JobDocument> searchJobsFlexible(String keyword, String location, String industry, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jobRepository.searchJobsFlexible(keyword, location, industry, pageable);
    }

    //Helper
    private String getClientIp(HttpServletRequest request) {
        String clientIp;

        // Kiểm tra X-Forwarded-For để có thể lấy IP thực tế trong trường hợp có proxy/load balancer
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            clientIp = xForwardedFor.split(",")[0];  // Lấy IP đầu tiên (người dùng)
        } else {
            clientIp = request.getRemoteAddr();  // Nếu không có X-Forwarded-For, lấy IP từ request trực tiếp
        }

        return clientIp;
    }

    private String extractUserIdFromJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Jwt jwt = jwtDecoder.decode(token);
                // Lấy claim theo tên (ví dụ: "sub" là userId, hoặc "user_id", hoặc "username" tùy hệ thống của bạn)
                // Thường là "sub"
                log.info(String.valueOf(jwt));
                return jwt.getSubject();
                // Hoặc: return jwt.getClaimAsString("user_id");
            } catch (JwtException ex) {
                // Token không hợp lệ hoặc hết hạn
                log.error(ex.getMessage());
                return null;
            }
        }
        return null;
    }
}
