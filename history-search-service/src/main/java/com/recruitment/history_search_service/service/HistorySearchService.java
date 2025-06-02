package com.recruitment.history_search_service.service;


import com.recruitment.history_search_service.dto.HistorySearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HistorySearchService {

    Page<HistorySearchDTO> getList(Pageable pageable);

    Page<HistorySearchDTO> getListByUserId(String userId, Pageable pageable);

    void deleteOne(UUID id);

    void deleteAll();

    void saveSearch(HistorySearchDTO dto);
}

