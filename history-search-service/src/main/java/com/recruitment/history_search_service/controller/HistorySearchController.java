package com.recruitment.history_search_service.controller;

import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.history_search_service.dto.HistorySearchDTO;
import com.recruitment.history_search_service.service.HistorySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history-search")
public class HistorySearchController {

    private final HistorySearchService service;

    @GetMapping
    public ResponseEntity< ApiResponse<PageResponse<HistorySearchDTO>>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<PageResponse<HistorySearchDTO>> apiResponse = ApiResponse.<PageResponse<HistorySearchDTO>>builder()
                .result(service.getList(PageRequest.of(page, size)))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("{userId}/user")
    public ResponseEntity<ApiResponse<PageResponse<HistorySearchDTO>>> getListByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<PageResponse<HistorySearchDTO>> apiResponse = ApiResponse.<PageResponse<HistorySearchDTO>>builder()
                .result(service.getListByUserId(userId, PageRequest.of(page, size)))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable UUID id) {
        service.deleteOne(id);
    }

    @DeleteMapping
    public void deleteByUserId(String userId) {
        service.deleteByUserId(userId);
    }
}

