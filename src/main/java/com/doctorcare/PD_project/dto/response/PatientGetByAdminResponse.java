package com.doctorcare.PD_project.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientGetByAdminResponse {

    String id;
    String name;
    LocalDate dob;
    String username;
    String phone;
    String email;
    String gender;
    String img;
    String address;
    boolean enable;
    boolean blocked;
}
