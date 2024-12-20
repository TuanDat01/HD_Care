package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String content;
    String img;
    Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
