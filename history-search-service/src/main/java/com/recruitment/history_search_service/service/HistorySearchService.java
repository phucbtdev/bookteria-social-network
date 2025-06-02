package com.recruitment.history_search_service.service;


import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.history_search_service.dto.HistorySearchDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface HistorySearchService {

    PageResponse<HistorySearchDTO> getList(Pageable pageable);

    PageResponse<HistorySearchDTO> getListByUserId(String userId, Pageable pageable);

    void deleteOne(UUID id);

    void deleteByUserId(String userId);

    void saveSearch(HistorySearchDTO dto);
}

