package com.recruitment.history_search_service.service.impl;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.history_search_service.dto.HistorySearchDTO;
import com.recruitment.history_search_service.entity.HistorySearch;
import com.recruitment.history_search_service.mapper.HistorySearchMapper;
import com.recruitment.history_search_service.repository.HistorySearchRepository;
import com.recruitment.history_search_service.service.HistorySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistorySearchServiceImpl implements HistorySearchService {

    private final HistorySearchRepository repository;
    private final HistorySearchMapper mapper;

    @Override
    public PageResponse<HistorySearchDTO> getList(Pageable pageable) {
        Page<HistorySearch> pageHistory = repository.findAll(pageable);
        List<HistorySearchDTO> listHistory = pageHistory.getContent().stream().map(mapper::entityToDto).toList();

        return PageResponse.<HistorySearchDTO>builder()
                .pageNo(pageHistory.getNumber())
                .pageSize(pageHistory.getSize())
                .totalElements(pageHistory.getNumberOfElements())
                .totalPages(pageHistory.getTotalPages())
                .data(listHistory)
                .build();
    }

    @Override
    public PageResponse<HistorySearchDTO> getListByUserId(String userId, Pageable pageable) {
        Page<HistorySearch> pageHistory = repository.findAllByUserId(userId, pageable);
        List<HistorySearchDTO> listHistory = pageHistory.getContent().stream().map(mapper::entityToDto).toList();

        return PageResponse.<HistorySearchDTO>builder()
                .pageNo(pageHistory.getNumber())
                .pageSize(pageHistory.getSize())
                .totalElements(pageHistory.getNumberOfElements())
                .totalPages(pageHistory.getTotalPages())
                .data(listHistory)
                .build();
    }

    @Override
    public void deleteOne(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByUserId(String userId) {
        repository.deleteByUserId(userId);
    }

    @Override
    public void saveSearch(HistorySearchDTO dto) {
        HistorySearch entity = new HistorySearch();
        BeanUtils.copyProperties(dto, entity);
        repository.save(entity);
    }
}

