package com.doctorcare.PD_project.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class ManagePatient {
    String id;
    String name;
    LocalDate dob;
    LocalDateTime date;
    String address;
    String email;
    String phone;
    Long count;
}
