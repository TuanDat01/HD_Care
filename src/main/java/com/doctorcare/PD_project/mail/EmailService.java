package com.doctorcare.PD_project.mail;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String htmlContent, byte[] data) throws MessagingException;
}
