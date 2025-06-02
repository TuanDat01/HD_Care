package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    Appointment appointment;

    String transactionId;      // Mã VNPAY đầu vào
    double amount;             // Số tiền thanh toán
    String status;             // PENDING / SUCCESS / FAILED
    LocalDateTime createdAt;

    @Column(columnDefinition = "LONGTEXT")
    String paymentUrl;         // URL thanh toán

    // --- MỞ RỘNG CHO HOÀN TIỀN ---
    String refundTransactionId;
    double refundAmount;
    String refundStatus;       // PENDING / SUCCESS / FAILED
    LocalDateTime refundedAt;
}
