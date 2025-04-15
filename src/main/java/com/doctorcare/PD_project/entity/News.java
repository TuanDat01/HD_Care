package com.doctorcare.PD_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "news")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    User author;

    String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    String content;

    String category;

    // COVER IMAGE URL
    String coverImageUrl;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    @Builder.Default
    User approvedBy = null;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    User assignedTo;

    boolean isApproved;

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    boolean isDraft = false;

    @Builder.Default
    int interactUseful = 0;

    @Builder.Default
    int interactUseless = 0;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<NewsInteraction> interactions;
}