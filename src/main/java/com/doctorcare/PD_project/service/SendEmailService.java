package com.doctorcare.PD_project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.enums.AppointmentStatus;
import com.doctorcare.PD_project.event.create.OnRegisterEvent;
import com.doctorcare.PD_project.mail.EmailServiceImpl;
import com.doctorcare.PD_project.mapping.AppointmentMapper;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class SendEmailService {
    final private EmailServiceImpl emailServiceImpl;
    AppointmentMapper appointmentMapper;

    public SendEmailService(EmailServiceImpl emailServiceImpl) {
        this.emailServiceImpl = emailServiceImpl;
    }

    @Async
    public void sendAppointmentConfirmation(Appointment savedAppointment,boolean isCancel,String note) throws MessagingException {
        String title = isCancel
                ? "Notice of appointment cancellation"
                : "Appointment successful notification";
        String reason = StringUtils.hasText(note)
                ? "Reason for change appointment: " + note
                : "";
        String subject = savedAppointment
                .getSchedule()
                .getStart()
                .format(DateTimeFormatter.ofPattern("hh:mm")) + "-" +savedAppointment.getSchedule().getEnd().format(DateTimeFormatter.ofPattern("hh:mm")) ;
        System.out.println(savedAppointment.getPatient().getEmail());
        String htmlContent = String.format("<h1>%s : <strong>%s</strong></h1>"
                + "<p>Information patient : <strong>%s</strong> </p>"
                + "<p>Time appointment <strong>%s</strong>" +
                        "<p>%s</p>"
                ,title,savedAppointment.getSchedule().getStart().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),savedAppointment.getPatient().getName(), subject,reason);
        emailServiceImpl.sendSimpleMessage(savedAppointment.getPatient().getEmail(),"INFORMATION HD_CARE",htmlContent,null);

        System.out.println("Execute method asynchronously. "
                + Thread.currentThread().getName());
    }
    @Async
    public void sendPrescription(AppointmentRequest appointmentRequest,byte[] data) throws MessagingException {
        String subject = "Prescription";
        String htmlContent = String.format("<h1>Prescription</h1>"
                + "<p>Prescription for patient : <strong>%s</strong> </p>",appointmentRequest.getName());
        emailServiceImpl.sendSimpleMessage(appointmentRequest.getEmail(), subject, htmlContent,data);
    }

    @Async
    public void sendReview(AppointmentRequest appointmentRequest) throws MessagingException {
        String subject = "Review Doctor :" + appointmentRequest.getNameDoctor();
        String htmlContent = String.format("<h1>Thanks for your trust</h1>"
                + "<p>Please leave review for doctor: <strong>%s</strong> </p>" +
                "<p>Follow link : <a href = 'http://localhost:3000/home?evaluate=1&idAppointment=%s'>Link review</a></p>",
                appointmentRequest.getNameDoctor(),
                appointmentRequest.getId());
        emailServiceImpl.sendSimpleMessage(appointmentRequest.getEmail(), subject, htmlContent, null);
    }

    @Async
    public void sendActive(String token, OnRegisterEvent event) throws MessagingException {
        String url = "http://localhost:8082/api/v1/auth/verify?token=" + token;
        String subject = String.format("Xác nhận tài khoản" +
                "<p>Chao ban: <strong>%s</strong></p>" +
                "<p>Xin vui long kich hoat tai khoan : <a href = '%s'>o day</a></p>", event.getUser().getName(), url);
        emailServiceImpl.sendSimpleMessage(event.getUser().getEmail(), "Xác nhận tài khoản", subject, null);
    }

    @Async
    public void sendCancelAppointment(String token, OnRegisterEvent event) throws MessagingException {
        String url = event.getAppUrl() + "/verify?token=" + token;
        String subject = String.format("Xác nhận tài khoản" +
                "<p>Chao ban: <strong>%s</strong></p>" +
                "<p>Xin vui long kich hoat tai khoan : <a href = '%s'>o day</a></p>", event.getUser().getName(), url);
        emailServiceImpl.sendSimpleMessage(event.getUser().getEmail(), "Xác nhận tài khoản", subject, null);
    }

    public List<String> sendImage(List<MultipartFile> file2) {
        List<String> img = new ArrayList<>();
            Dotenv dotenv = Dotenv.load();
            Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));

            for (int i = 0; i < file2.size(); i++) {
                String publicId = LocalDateTime.now().toString() + i;
                Map params1 = ObjectUtils.asMap(
                        "public_id", publicId,
                        "use_filename", true,
                        "unique_filename", false
                        );
                try {
                        // Upload file lên Cloudinary và trả về URL
                Map uploadResult = cloudinary.uploader().upload(file2.get(i).getBytes(), params1);
                       img.add(uploadResult.get("secure_url").toString());
                    } catch (IOException e) {
                        System.err.println("Error uploading image: " + e.getMessage());
                        return null;  // Có thể xử lý lỗi ở đây
                    }
                };

        return img;
    }

}
