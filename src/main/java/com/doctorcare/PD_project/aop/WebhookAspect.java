package com.doctorcare.PD_project.aop;

import com.doctorcare.PD_project.annotation.EventTrigger;
import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.dto.response.NotificateDTO;
import com.doctorcare.PD_project.entity.Notification;
import com.doctorcare.PD_project.entity.Post;
import com.doctorcare.PD_project.enums.NotificationType;
import com.doctorcare.PD_project.mapping.AppointmentMapper;
import com.doctorcare.PD_project.mapping.HasUser;
import com.doctorcare.PD_project.mapping.NotificateMap;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.service.NotificationService;
import com.doctorcare.PD_project.service.PatientService;
import com.doctorcare.PD_project.service.SendNotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@RequiredArgsConstructor
@Component
public class WebhookAspect {
    private final SendNotificationService sendNotificationService;
    private final NotificateMap notificateMap;
    private final NotificationService notificationService;
    private final AppointmentMapper appointmentMapper;
    private final UserMapper userMapper;
    @AfterReturning(pointcut = "@annotation(trigger)", returning = "result")
    public void triggerWebHook(Object result, EventTrigger trigger) {
        NotificationType notificationType = trigger.event();
        Notification notification = new Notification();
        HasUser hasUser = null;
        if (result instanceof HasUser) {
            hasUser = (HasUser) result;
        }
        String receiver_user = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> params = new HashMap<>();

        if (NotificationType.POST.contains(notificationType)){
            Post post = (Post)result;
            NotificateDTO notificateDTO = notificateMap.toNotificateDTO(hasUser);
            params.put("{user}", receiver_user);
            if (notificateDTO != null){
                notification.setReceiver(notificateDTO.getUser());
                notification.setIdReference(post.getId());
            }
        }
        else if (NotificationType.COMMENT_FOLLOW.contains(notificationType)) {
            NotificateDTO notificateDTO = notificateMap.toNotificateDTO(hasUser);
            params.put("{user}", receiver_user);
            notification.setReceiver(notificateDTO.getPost().getUser());
            notification.setIdReference(notificateDTO.getPost().getId());

        }
        else{
            AppointmentRequest appointmentRequest = (AppointmentRequest) result;
            params.put("{doctor}",((AppointmentRequest) result).getNameDoctor());
            params.put("{date}", appointmentRequest.getStart());
            notification.setReceiver(appointmentRequest.getPatient());
            notification.setIdReference(appointmentRequest.getId());
        }
        String message = replacePlaceholders(notificationType.getMessage(), params);

        notification.setMessage(message);
        notification.setEvent_type(notificationType.getType());
        notification.setRead(false);
        notificationService.saveNotification(notification);

        NotificationMessage notificationMessage = notificateMap.toNotificationMessage(notification);
        notificationMessage.setData(result);
        sendNotificationService.sendNotification(notificationMessage);
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
