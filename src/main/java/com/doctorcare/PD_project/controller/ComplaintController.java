package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.Complaint;
import com.doctorcare.PD_project.service.ComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/complaint")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ComplaintController {
    ComplaintService complaintService;

    /**
     * Khách hàng gửi khiếu nại
     */
    @PostMapping
    public ApiResponse<Complaint> submit(
            @RequestParam String appointmentId,
            @RequestBody String reason) {
        Complaint c = complaintService.submitComplaint(appointmentId, reason);
        return ApiResponse.<Complaint>builder().result(c).build();
    }

    /**
     * Admin review khiếu nại
     */
    @PutMapping("/{id}/review")
    public ApiResponse<Complaint> review(
            @PathVariable String id,
            @RequestParam boolean approve,
            @RequestParam String notes,
            HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        Complaint c = complaintService.reviewComplaint(id, approve, notes, ip);
        return ApiResponse.<Complaint>builder().result(c).build();
    }
}