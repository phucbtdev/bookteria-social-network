package com.recruitment.history_search_service.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment.history_search_service.dto.HistorySearchDTO;
import com.recruitment.history_search_service.service.HistorySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistorySearchListener {

    private final HistorySearchService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "search-history-topic")
    public void listenSearchEvent(String message) {
        log.info("listenSearchEvent: {}", message);
        try {
            HistorySearchDTO dto = objectMapper.readValue(message, HistorySearchDTO.class);
            service.saveSearch(dto);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
