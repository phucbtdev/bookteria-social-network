package com.recruitment.history_search_service.service;

import com.recruitment.common.dto.response.PageResponse;
import com.recruitment.history_search_service.dto.HistorySearchDTO;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Interface định nghĩa các nghiệp vụ (service) liên quan đến quản lý lịch sử tìm kiếm.
 *
 * Các phương thức bao gồm:
 * - Lấy danh sách lịch sử tìm kiếm (có phân trang)
 * - Lấy lịch sử tìm kiếm theo userId (có phân trang)
 * - Xóa lịch sử tìm kiếm theo id hoặc theo userId
 * - Lưu một bản ghi lịch sử tìm kiếm mới
 */
public interface HistorySearchService {

    /**
     * Lấy danh sách tất cả các bản ghi lịch sử tìm kiếm, hỗ trợ phân trang.
     *
     * @param pageable Đối tượng phân trang (số trang, kích thước trang, sắp xếp)
     * @return Đối tượng PageResponse chứa danh sách HistorySearchDTO trong trang
     */
    PageResponse<HistorySearchDTO> getList(Pageable pageable);

    /**
     * Lấy danh sách lịch sử tìm kiếm của một người dùng cụ thể, hỗ trợ phân trang.
     *
     * @param userId Mã định danh của người dùng (ứng viên)
     * @param pageable Đối tượng phân trang (số trang, kích thước trang, sắp xếp)
     * @return Đối tượng PageResponse chứa danh sách HistorySearchDTO trong trang của user
     */
    PageResponse<HistorySearchDTO> getListByUserId(String userId, Pageable pageable);

    /**
     * Xóa một bản ghi lịch sử tìm kiếm theo ID duy nhất.
     *
     * @param id UUID của bản ghi lịch sử cần xóa
     */
    void deleteOne(UUID id);

    /**
     * Xóa tất cả các bản ghi lịch sử tìm kiếm của một người dùng theo userId.
     *
     * @param userId Mã định danh của người dùng (ứng viên)
     */
    void deleteByUserId(String userId);

    /**
     * Lưu một bản ghi lịch sử tìm kiếm mới.
     *
     * @param dto Đối tượng HistorySearchDTO chứa thông tin tìm kiếm cần lưu
     */
    void saveSearch(HistorySearchDTO dto);
}
