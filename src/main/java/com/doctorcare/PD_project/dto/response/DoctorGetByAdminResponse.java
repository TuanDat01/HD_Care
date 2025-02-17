package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Review;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorGetByAdminResponse {

    String id;
    String name;
    String username;
    String phone;
    String clinicName;
    String district;
    String city;
    String email;
    String gender;
    String specialization;
    String experience;
    long price;
    String description;
    String img;
    String address;
    boolean enable;
    boolean blocked;
}