package com.recruitment.history_search_service.controller;

import com.recruitment.history_search_service.dto.HistorySearchDTO;
import com.recruitment.history_search_service.service.HistorySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history-search")
public class HistorySearchController {

    private final HistorySearchService service;

    @GetMapping
    public Page<HistorySearchDTO> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getList(PageRequest.of(page, size));
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable UUID id) {
        service.deleteOne(id);
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }
}

