package com.doctorcare.PD_project.event.create;

import com.doctorcare.PD_project.entity.Appointment;
import lombok.*;
import lombok.experimental.FieldDefaults;
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentStatusChange {
    Appointment appointment;
    boolean isCancel;
    String note;
}
