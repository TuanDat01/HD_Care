package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseService sseService;

    public Mono<Void> pushNotification(NotificationMessage notification) {
        return sseService.sendToUser(notification.getUsername(), notification);
    }
}
