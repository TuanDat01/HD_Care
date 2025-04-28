package com.doctorcare.PD_project.aop;

import com.doctorcare.PD_project.annotation.EventTrigger;
import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.service.PatientService;
import com.doctorcare.PD_project.service.SendNotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Aspect
@RequiredArgsConstructor
@Component
public class WebhookAspect {
    private final SendNotificationService notificationService;
    private final PatientService patientService;
    @AfterReturning(pointcut = "@annotation(trigger)", returning = "result")
    public void triggerWebHook(Object result, EventTrigger trigger) {
        String eventType = trigger.event();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        NotificationMessage notificationMessage = NotificationMessage
                .builder()
                .event_type(eventType)
                .username(authentication.getName())
                .data(result) // dùng result nè
                .build();
        System.out.println("data send" + notificationMessage.getUsername());
        notificationService.sendNotification(notificationMessage);
    }


}
