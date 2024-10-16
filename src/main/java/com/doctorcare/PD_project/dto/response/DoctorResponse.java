package com.doctorcare.PD_project.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorResponse {
    String id;
    String name;
    String location;

    String specialization;

    String experience;

    long price;

    String description;

}
