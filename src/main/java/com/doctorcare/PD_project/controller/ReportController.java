package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.ReportRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.ReportDetailResponse;
import com.doctorcare.PD_project.dto.response.ReportedPostResponse;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.ReportService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social/report-post")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {

    ReportService reportService;

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> reportPost(
            @PathVariable String postId,
            @Valid @RequestBody ReportRequest dto) throws AppException {
        reportService.reportPost(postId, dto);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Report submitted")
                .build());
    }

    // Lấy danh sách post bị báo cáo
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReportedPostResponse>>> listReportedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        var result = reportService.listReportedPosts(page, size);
        return ResponseEntity.ok(ApiResponse.<Page<ReportedPostResponse>>builder()
                .code(1000)
                .result(result)
                .build());
    }

    // Lấy chi tiết các báo cáo của một post
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<Page<ReportDetailResponse>>> getReportsByPost(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws AppException {
        var result = reportService.getReportsByPost(postId, page, size);
        return ResponseEntity.ok(ApiResponse.<Page<ReportDetailResponse>>builder()
                .code(1000)
                .result(result)
                .build());
    }

    // Duyệt và xóa post
    @PostMapping("/{postId}/resolve")
    public ResponseEntity<ApiResponse<Void>> resolveReports(
            @PathVariable String postId) throws AppException {
        reportService.resolveReportsForPost(postId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Post deleted and reports resolved")
                .build());
    }

    // Từ chối một báo cáo riêng lẻ
    @PostMapping("/reject/{reportId}")
    public ResponseEntity<ApiResponse<Void>> rejectReport(
            @PathVariable String reportId) throws AppException {
        reportService.rejectReport(reportId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .code(1000)
                .message("Report rejected")
                .build());
    }
}
