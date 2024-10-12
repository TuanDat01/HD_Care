package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    String content;

    @ElementCollection
    List<String> image;

    @OneToMany
    @JoinColumn(name = "comment_id")
    List<Comment> comments;

    @OneToMany
    @JoinColumn(name = "like_id")
    List<Like> likes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

}
