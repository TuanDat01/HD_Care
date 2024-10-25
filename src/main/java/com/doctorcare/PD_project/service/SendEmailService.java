package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.mail.EmailServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
@Service
public class SendEmailService {
    @Autowired
    EmailServiceImpl emailServiceImpl;
    @Async
    public void sendAppointmentConfirmation(Appointment savedAppointment) throws MessagingException {
        String subject = savedAppointment.getSchedule().getStart().format(DateTimeFormatter.ofPattern("hh:mm")) + "-" +savedAppointment.getSchedule().getEnd().format(DateTimeFormatter.ofPattern("hh:mm")) ;
        System.out.println(savedAppointment.getPatient().getEmail());
        String htmlContent = String.format("<h1>Xác nhận đặt lịch khám</h1>"
                + "<p>Xác nhận lịch khám cho bệnh nhân : <strong>%s</strong> </p>"
                + "<p>Thời gian khám <strong>%s</strong>",savedAppointment.getPatient().getName(), subject);
        emailServiceImpl.sendSimpleMessage(savedAppointment.getPatient().getEmail(),"Xác nhận đặt lịch",htmlContent);

        System.out.println("Execute method asynchronously. "
                + Thread.currentThread().getName());
    }
}
