package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    VNPayService vnPayService;

    /**
     * Tạo URL thanh toán VNPAY
     */
    @PostMapping("/vnpay")
    public ApiResponse<String> createPaymentUrl(
            @RequestParam String appointmentId,
            HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String url = vnPayService.createPaymentUrl(appointmentId, ip);
        return ApiResponse.<String>builder().result(url).build();
    }

    /**
     * Hoàn tiền cho giao dịch
     */
    @PostMapping("/vnpay/refund")
    public ApiResponse<Boolean> refund(
            @RequestParam String transactionId,
            HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        boolean result = vnPayService.refundPayment(transactionId, ip);
        return ApiResponse.<Boolean>builder().result(result).build();
    }
}