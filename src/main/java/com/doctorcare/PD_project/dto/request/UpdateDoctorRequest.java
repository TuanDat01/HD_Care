package com.doctorcare.PD_project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateDoctorRequest {
    String username;
    String name;
    @Size(min = 10, max = 10, message = "PHONE_NUMBER_PATTERN")
    String phone;
    String address;
    String gender;
    String city;
    String district;
    String img;
    @Email(message = "INVALID_FORMAT")
    String email;
    String specialization;
    String experience;
    long price;
    String description;
    String clinicName;

}
