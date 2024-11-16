package com.doctorcare.PD_project.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService  {
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String htmlContent, byte[] data) throws MessagingException {
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom("tuandatt2003@gmail.com");
//        simpleMailMessage.setTo(to);
//        simpleMailMessage.setText(text);
//        simpleMailMessage.setSubject(subject);
//        javaMailSender.send(simpleMailMessage);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
        mimeMessageHelper.setFrom("tuandatt2003@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(htmlContent,true);
        if (data!= null) {
            ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(data, "application/pdf");
            mimeMessageHelper.addAttachment("Prescription.pdf", byteArrayDataSource);
        }
        javaMailSender.send(mimeMessage);
        System.out.println(mimeMessage);

    }
}
