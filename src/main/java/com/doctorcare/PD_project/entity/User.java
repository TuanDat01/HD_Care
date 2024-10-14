package com.doctorcare.PD_project.entity;

import com.doctorcare.PD_project.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    String id;

    String name;

    String email;

    String username;

    @Column(name = "password")
    String pwd;

    String phone;

    String gender;

    @Column(name = "birthday")
    LocalDate dob;

    @Column(name = "profile_img")
    String img;

    String role;

    boolean isEnable;

    boolean isBlocked;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Post> posts;

}
