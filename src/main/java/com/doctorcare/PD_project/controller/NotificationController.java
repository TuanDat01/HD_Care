package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/webhook/receive")
    public Mono<Void> receiveWebhook(@RequestBody NotificationMessage notification) {
        System.out.println("data process" + notification.getUsername());
        return notificationService.pushNotification(notification);
    }
}
