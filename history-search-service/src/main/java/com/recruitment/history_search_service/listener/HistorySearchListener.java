package com.recruitment.history_search_service.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.history_search_service.dto.HistorySearchDTO;
import com.recruitment.history_search_service.service.HistorySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistorySearchListener {

    private final HistorySearchService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "search-history-topic")
    public void listenSearchEvent(String message) {
        try {
            HistorySearchDTO dto = objectMapper.readValue(message, HistorySearchDTO.class);
            service.saveSearch(dto);
        } catch (Exception e) {
            // log lỗi hoặc xử lý
            e.printStackTrace();
        }
    }
}
