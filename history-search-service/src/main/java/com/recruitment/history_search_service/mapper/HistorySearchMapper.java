package com.recruitment.history_search_service.mapper;

import com.recruitment.history_search_service.dto.HistorySearchDTO;
import com.recruitment.history_search_service.entity.HistorySearch;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistorySearchMapper {
    HistorySearchDTO entityToDto(HistorySearch entity);
}
