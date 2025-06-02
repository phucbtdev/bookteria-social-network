package com.recruitment.history_search_service.service.impl;

import com.recruitment.history_search_service.dto.HistorySearchDTO;
import com.recruitment.history_search_service.entity.HistorySearch;
import com.recruitment.history_search_service.repository.HistorySearchRepository;
import com.recruitment.history_search_service.service.HistorySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistorySearchServiceImpl implements HistorySearchService {

    private final HistorySearchRepository repository;

    @Override
    public Page<HistorySearchDTO> getList(Pageable pageable) {
        Page<HistorySearch> page = repository.findAll(pageable);
        return page.map(entity -> {
            HistorySearchDTO dto = new HistorySearchDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        });
    }

    @Override
    public void deleteOne(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void saveSearch(HistorySearchDTO dto) {
        HistorySearch entity = new HistorySearch();
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
    }
}

