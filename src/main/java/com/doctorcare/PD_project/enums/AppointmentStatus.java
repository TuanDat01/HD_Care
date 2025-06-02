package com.doctorcare.PD_project.enums;

public enum AppointmentStatus {
    PENDING,       // Chưa thanh toán
    PAID,          // Đã thanh toán thành công
    CONFIRMED,     // Đã đặt lịch (có thể dùng tương đương PAID+CONFIRMED)
    IN_PROGRESS,   // Đang khám
    COMPLETED,     // Khám xong
    CANCELLED,     // Bị hủy
    REFUNDED       // Đã hoàn tiền
}
