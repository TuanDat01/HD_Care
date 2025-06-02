package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.dto.response.BasicInfoUserResponse;
import com.doctorcare.PD_project.mapping.HasUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Post implements HasUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PostImage> images;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Like> likes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    User doctor;

    int countLikes = 0;
    int countComments = 0;

    boolean isHidden;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Override
    public BasicInfoUserResponse getUserResponse() {
        return null;
    }

    @Override
    public Patient getPatient() {
        return null;
    }

    @Override
    public Post getPost() {
        return null;
    }
}
