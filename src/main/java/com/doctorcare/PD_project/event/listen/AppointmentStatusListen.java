package com.doctorcare.PD_project.event.listen;

import com.doctorcare.PD_project.annotation.EventTrigger;
import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.event.create.AppointmentStatusChange;
import com.doctorcare.PD_project.service.SendEmailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentStatusListen {
    SendEmailService sendEmailService;
    @EventListener
    @EventTrigger(event = "STATUS")
    public Appointment sendMailForChange(AppointmentStatusChange appointmentStatusChange) throws MessagingException {
        sendEmailService.sendAppointmentConfirmation(appointmentStatusChange.getAppointment(),appointmentStatusChange.isCancel(),appointmentStatusChange.getNote());
        return appointmentStatusChange.getAppointment();
    }

}
