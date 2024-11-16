package com.doctorcare.PD_project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class VerifyToken {
    final static int EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String token;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    User user;
    Date expiryDate;
    Date expire(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE, EXPIRATION);
        return calendar.getTime();
    }


    public VerifyToken(String token,User user)
    {
        this.token = token;
        this.user = user;
        this.expiryDate = expire();
    }
}
