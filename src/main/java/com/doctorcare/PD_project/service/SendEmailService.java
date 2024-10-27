package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.mail.EmailServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
@Service
public class SendEmailService {
    final private EmailServiceImpl emailServiceImpl;

    public SendEmailService(EmailServiceImpl emailServiceImpl) {
        this.emailServiceImpl = emailServiceImpl;
    }

    @Async
    public void sendAppointmentConfirmation(Appointment savedAppointment) throws MessagingException {
        String subject = savedAppointment.getSchedule().getStart().format(DateTimeFormatter.ofPattern("hh:mm")) + "-" +savedAppointment.getSchedule().getEnd().format(DateTimeFormatter.ofPattern("hh:mm")) ;
        System.out.println(savedAppointment.getPatient().getEmail());
        String htmlContent = String.format("<h1>Verify Appointment Date: <strong>%s</strong></h1>"
                + "<p>Verify appointment for patient : <strong>%s</strong> </p>"
                + "<p>Time appointment <strong>%s</strong>",savedAppointment.getSchedule().getStart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),savedAppointment.getPatient().getName(), subject);
        emailServiceImpl.sendSimpleMessage(savedAppointment.getPatient().getEmail(),"Verify appointment",htmlContent);

        System.out.println("Execute method asynchronously. "
                + Thread.currentThread().getName());
    }
}
