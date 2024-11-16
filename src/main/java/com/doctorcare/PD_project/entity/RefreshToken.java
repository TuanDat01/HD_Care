//package com.doctorcare.PD_project.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.time.Instant;
//
//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class RefreshToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "id", nullable = false)
//    String id;
//
//    Instant expireDate;
//
//    @Column(nullable = false, unique = true)
//    String token;
//
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    User user;
//
//
//}
