package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    User receiver; // Người nhận thông báo

    @ManyToOne
    @JoinColumn(name = "sender_id")
    User sender; // Người gửi thông báo (nếu có)

    String message; // Nội dung thông báo
    String event_type;

    boolean isRead = false; // Trạng thái đọc

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt = LocalDateTime.now();
    String idReference;
}
