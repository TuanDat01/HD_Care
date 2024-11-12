package com.doctorcare.PD_project.event.listen;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.event.create.CompleteAppointment;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.responsitory.AppointmentRepository;
import com.doctorcare.PD_project.service.AppointmentService;
import com.doctorcare.PD_project.service.SendEmailService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompleteAppointmentListen {
    AppointmentRepository  appointmentRepository;
    SendEmailService sendEmailService;
    @Transactional
    @EventListener
    public void sendMailForChange(CompleteAppointment completeAppointment) throws MessagingException, AppException {
        Appointment appointment = appointmentRepository.findById(completeAppointment.getAppointmentRequest().getId()).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_APPOINTMENT));
        appointment.setStatus(AppointmentStatus.COMPLETED.toString());
        sendEmailService.sendPrescription(completeAppointment.getAppointmentRequest(), completeAppointment.getPdfData());
        sendEmailService.sendReview(completeAppointment.getAppointmentRequest());

    }
}
