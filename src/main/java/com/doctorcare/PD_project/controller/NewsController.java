package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.NewsCreateRequest;
import com.doctorcare.PD_project.dto.request.NewsUpdateRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.NewsResponse;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    // Tạo một tin tức mới
    @PostMapping
    public ResponseEntity<ApiResponse<NewsResponse>> createNews(@RequestBody NewsCreateRequest req) throws AppException {
        NewsResponse result = newsService.createNews(req);
        return ResponseEntity.ok(ApiResponse.<NewsResponse>builder()
                .code(1000)
                .message("News created successfully")
                .result(result)
                .build());
    }

    // Lấy tin tức theo ID
    @GetMapping("/{newsId}")
    public ResponseEntity<ApiResponse<NewsResponse>> getNewsById(@PathVariable String newsId) throws AppException {
        NewsResponse result = newsService.getNewsById(newsId);
        return ResponseEntity.ok(ApiResponse.<NewsResponse>builder()
                .code(1000)
                .message("News fetched successfully")
                .result(result)
                .build());
    }

    // Lấy tất cả tin tức với phân trang (chỉ lấy tin đã duyệt và không phải tin nháp)
    @GetMapping
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.getAllNews(page, size);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("News list fetched successfully")
                .result(result)
                .build());
    }

    // Lấy tin tức mới nhất
    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getNewestNews(
            @RequestParam(defaultValue = "5") int limit) throws AppException {
        List<NewsResponse> result = newsService.getNewestNews(limit);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Newest news fetched successfully")
                .result(result)
                .build());
    }

    // Lấy tin tức nổi bật (theo lượt tương tác hữu ích)
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getFeaturedNews(
            @RequestParam(defaultValue = "5") int limit) throws AppException {
        List<NewsResponse> result = newsService.getFeaturedNews(limit);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Featured news fetched successfully")
                .result(result)
                .build());
    }

    // Lấy tin tức theo danh mục với phân trang
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getNewsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.getNewsByCategory(category, page, size);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("News by category fetched successfully")
                .result(result)
                .build());
    }

    // Tìm tin tức theo từ khóa với phân trang
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> searchNews(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.searchNews(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Search news fetched successfully")
                .result(result)
                .build());
    }

    // Lấy tin tức theo bác sĩ và trạng thái (chỉ dành cho bác sĩ)
    @GetMapping("/doctor")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getDoctorNewsByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.getNewsByDoctorAndStatus(status, page, size);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Doctor news fetched successfully for status: " + status)
                .result(result)
                .build());
    }

    // Lấy tin tức theo bác sĩ và trạng thái (chỉ dành cho bác sĩ)
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getDoctorNewsByDoctorId(
            @PathVariable String doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.getDoctorNewsByDoctorId(doctorId, page, size);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Doctor news fetched successfully for doctorId: " + doctorId)
                .result(result)
                .build());
    }

    // Cập nhật tin tức (dành cho tin nháp)
    @PutMapping("/{newsId}")
    public ResponseEntity<ApiResponse<NewsResponse>> updateNews(
            @PathVariable String newsId,
            @RequestBody NewsUpdateRequest newsUpdateRequest) throws AppException {
        NewsResponse result = newsService.updateNews(newsId, newsUpdateRequest);
        return ResponseEntity.ok(ApiResponse.<NewsResponse>builder()
                .code(1000)
                .message("News updated successfully")
                .result(result)
                .build());
    }

    // Xóa tin tức
    @DeleteMapping("/{newsId}")
    public ResponseEntity<ApiResponse<Void>> deleteNews(@PathVariable String newsId) throws AppException {
        newsService.deleteNews(newsId);
        return ResponseEntity.noContent().build();
    }

    // Duyệt tin tức (admin hoặc bác sĩ)
    @PostMapping("/{newsId}")
    public ResponseEntity<ApiResponse<NewsResponse>> approveNews(@PathVariable String newsId,
                                                                 @RequestParam boolean approve) throws AppException {
        NewsResponse result = newsService.approveNews(newsId, approve);
        return ResponseEntity.ok(ApiResponse.<NewsResponse>builder()
                .code(1000)
                .message("News approved successfully")
                .result(result)
                .build());
    }

    // Lấy danh sách tin tức đã duyệt của bác sĩ (dành cho admin hoặc bác sĩ)
    @GetMapping("/doctor/assigned")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getDoctorAssignedNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.getAssignedNewsToDoctor(page, size);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Assigned news fetched successfully")
                .result(result)
                .build());
    }

    // Lấy danh sách tin tức đã duyệt của bác sĩ (dành cho admin hoặc bác sĩ)
    @GetMapping("/doctor/reviewed")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getDoctorReviewedNews(
            @RequestParam boolean approved,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.getNewsReviewedByDoctor(approved, page, size);
        String msg = approved ? "Approved news fetched" : "Rejected news fetched";
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message(msg + " successfully")
                .result(result)
                .build());
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getPendingNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.getPendingNews(page, size);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Pending news fetched successfully")
                .result(result)
                .build());
    }

    // Chuyển tin tức cho bác sĩ duyệt (dành cho admin)
    @PostMapping("/{newsId}/assign/{doctorId}")
    public ResponseEntity<ApiResponse<NewsResponse>> assignNewsToDoctor(@PathVariable String newsId, @PathVariable String doctorId) throws AppException {
        NewsResponse result = newsService.assignNewsToDoctor(newsId, doctorId);
        return ResponseEntity.ok(ApiResponse.<NewsResponse>builder()
                .code(1000)
                .message("News assigned to doctor successfully")
                .result(result)
                .build());
    }

    // Tương tác với tin tức: tăng lượt useful hoặc useless
    @PostMapping("/{newsId}/interact")
    public ResponseEntity<ApiResponse<Void>> interactWithNews(@PathVariable String newsId, @RequestParam boolean isUseful) throws AppException {
        newsService.interactWithNews(newsId, isUseful);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("News interaction processed")
                .build());
    }

    // Lưu/Xóa tin tức trong mục yêu thích
    @PostMapping("/{newsId}/favorite")
    public ResponseEntity<ApiResponse<Void>> saveNewsToFavorite(@PathVariable String newsId) throws AppException {
        newsService.saveNewsToFavorite(newsId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("News saved/unsaved to favorites successfully")
                .build());
    }

    // Lấy danh sách tin tức đã lưu vào mục yêu thích của người dùng (phân trang)
    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<List<NewsResponse>>> getAllFavoriteNews(@RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) throws AppException {
        List<NewsResponse> result = newsService.getAllFavoriteNews(page, size);
        return ResponseEntity.ok(ApiResponse.<List<NewsResponse>>builder()
                .code(1000)
                .message("Favorite news fetched successfully")
                .result(result)
                .build());
    }
}

/*

// **Mở rộng tính năng mới**

// Bình luận trên tin tức
@PostMapping("/{newsId}/comments")
public ResponseEntity<Void> commentOnNews(@PathVariable String newsId, @RequestParam String content) {
    newsService.commentOnNews(newsId, content);
    return ResponseEntity.ok().build();
}

// Lấy danh sách bình luận của tin tức
@GetMapping("/{newsId}/comments")
public ResponseEntity<List<String>> getComments(@PathVariable String newsId) {
    return ResponseEntity.ok(newsService.getComments(newsId));
}

// Tăng lượt xem của tin tức
@PostMapping("/{newsId}/view")
public ResponseEntity<Void> increaseViewCount(@PathVariable String newsId) {
    newsService.increaseViewCount(newsId);
    return ResponseEntity.ok().build();
}

*/

