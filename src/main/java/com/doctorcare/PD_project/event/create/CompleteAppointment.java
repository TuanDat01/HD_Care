package com.doctorcare.PD_project.event.create;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompleteAppointment  {
    AppointmentRequest appointmentRequest;
    byte[] pdfData;

}
