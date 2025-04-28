package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "user_webhook_subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcriptionWebhook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "event_type", length = 100, nullable = false)
    private String eventType;

    @Column(name = "callback_url", length = 255, nullable = false)
    private String callbackUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
