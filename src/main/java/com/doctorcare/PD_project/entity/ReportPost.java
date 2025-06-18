package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "report_posts")
public class ReportPost {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    User reporter;

    @Column(nullable = false)
    String content;              // Nội dung báo cáo

    @OneToMany(mappedBy = "reportPost", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ReportImage> images;    // Ảnh minh họa

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ReportStatus status;         // NEW, RESOLVED, REJECTED

    LocalDateTime createdAt;
}