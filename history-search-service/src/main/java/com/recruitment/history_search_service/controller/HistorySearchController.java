package com.recruitment.history_search_service.controller;

import com.recruitment.common.dto.response.ApiResponse;
import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.history_search_service.dto.HistorySearchDTO;
import com.recruitment.history_search_service.service.HistorySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller quản lý lịch sử tìm kiếm (History Search) của ứng viên.
 *
 * Cung cấp các API để lấy danh sách lịch sử tìm kiếm, lọc theo người dùng,
 * và xóa lịch sử tìm kiếm theo id hoặc theo userId.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/history-search")
public class HistorySearchController {

    private final HistorySearchService service;

    /**
     * Lấy danh sách tất cả lịch sử tìm kiếm, có hỗ trợ phân trang.
     *
     * @param page Số trang (bắt đầu từ 0), mặc định là 0
     * @param size Số bản ghi trên mỗi trang, mặc định là 10
     * @return ResponseEntity chứa dữ liệu trang (Page) lịch sử tìm kiếm
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<HistorySearchDTO>>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<PageResponse<HistorySearchDTO>> apiResponse = ApiResponse.<PageResponse<HistorySearchDTO>>builder()
                .result(service.getList(PageRequest.of(page, size)))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Lấy danh sách lịch sử tìm kiếm của một người dùng cụ thể theo userId,
     * có hỗ trợ phân trang.
     *
     * @param userId Mã định danh của người dùng (ứng viên)
     * @param page Số trang (bắt đầu từ 0), mặc định là 0
     * @param size Số bản ghi trên mỗi trang, mặc định là 10
     * @return ResponseEntity chứa dữ liệu trang (Page) lịch sử tìm kiếm của user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<HistorySearchDTO>>> getListByUserId(
            @PathVariable("userId") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<PageResponse<HistorySearchDTO>> apiResponse = ApiResponse.<PageResponse<HistorySearchDTO>>builder()
                .result(service.getListByUserId(userId, PageRequest.of(page, size)))
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    /**
     * Xóa một bản ghi lịch sử tìm kiếm theo id.
     *
     * @param id UUID của bản ghi lịch sử cần xóa
     */
    @DeleteMapping("/{id}")
    public void deleteOne(
            @PathVariable UUID id
    ) {
        service.deleteOne(id);
    }

    /**
     * Xóa tất cả lịch sử tìm kiếm của một người dùng theo userId.
     *
     * @param userId Mã định danh của người dùng (ứng viên)
     */
    @DeleteMapping("/user/{userId}")
    public void deleteByUserId(
            @PathVariable("userId") String userId
    ) {
        service.deleteByUserId(userId);
    }
}
