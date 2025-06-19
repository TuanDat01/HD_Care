package com.doctorcare.PD_project.event.listen;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Notification;
import com.doctorcare.PD_project.enums.NotificationType;
import com.doctorcare.PD_project.event.create.AppointmentStatusChange;
import com.doctorcare.PD_project.mapping.NotificateMap;
import com.doctorcare.PD_project.service.NotificationService;
import com.doctorcare.PD_project.service.SendEmailService;
import com.doctorcare.PD_project.service.SendNotificationService;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentStatusListen {
    SendEmailService sendEmailService;
    SendNotificationService sendNotificationService;
    NotificationService notificationService;
    NotificateMap notificateMap;


    @EventListener
    public Appointment sendMailForChange(AppointmentStatusChange appointmentStatusChange) throws MessagingException {
        Notification notification = new Notification();
        sendEmailService.sendAppointmentConfirmation(appointmentStatusChange.getAppointment(),appointmentStatusChange.isCancel(),appointmentStatusChange.getNote());

        Map<String, String> params = new HashMap<>();
        params.put("{doctor}", appointmentStatusChange.getAppointment().getDoctor().getName());
        params.put("{status}", appointmentStatusChange.getAppointment().getStatus());

        notification.setMessage(replacePlaceholders(NotificationType.CHANGE_STATUS.getMessage(), params));
        notification.setEvent_type(NotificationType.CHANGE_STATUS.getType());
        notification.setReceiver(appointmentStatusChange.getAppointment().getPatient());
        notification.setIdReference(appointmentStatusChange.getAppointment().getId());
        notification.setRead(false);
        notificationService.saveNotification(notification);

        sendNotificationService.sendNotification(notificateMap.toNotificationMessage(notification));

        return appointmentStatusChange.getAppointment();
    }

    public String replacePlaceholders(String message, Map<String, String> params) {
        if (message == null || params == null || params.isEmpty()) {
            return message;
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }

}
