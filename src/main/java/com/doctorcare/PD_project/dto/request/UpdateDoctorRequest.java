package com.doctorcare.PD_project.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateDoctorRequest {
    String district;

    String specialization;

    String experience;

    long price;

    String description;
    String clinicName;

}
