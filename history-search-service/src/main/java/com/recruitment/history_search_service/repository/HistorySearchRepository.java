package com.recruitment.history_search_service.repository;

import com.recruitment.history_search_service.entity.HistorySearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HistorySearchRepository extends JpaRepository<HistorySearch, UUID> {

    Page<HistorySearch> findAllByUserId(String userId, Pageable pageable);
}