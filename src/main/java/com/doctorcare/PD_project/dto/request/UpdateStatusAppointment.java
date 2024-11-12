package com.doctorcare.PD_project.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStatusAppointment {
    String idDoctor;
    String status;
    String note;
}
