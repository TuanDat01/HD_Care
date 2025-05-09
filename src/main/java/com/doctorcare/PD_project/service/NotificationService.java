package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.entity.Notification;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.respository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseService sseService;
    private final NotificationRepository notificationRepository;

    public Mono<Void> pushNotification(NotificationMessage notification) {
        return sseService.sendToUser(notification.getUsername(), notification);
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public Page<Notification> getPageNotification(Pageable pageable) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return notificationRepository.findAllByReceiverUsernameOrderByCreatedAtDesc(name, pageable);
    }

    public Notification updateNotification(String id) throws AppException {
        Notification notification = notificationRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.INVALID_NOTIFICATION_TYPE));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    public void updateAllNotification() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> notifications = notificationRepository.findAllByReceiverUsernameOrderByCreatedAtDesc(name);
        notifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public List<Notification> getAllNotification() {
        return notificationRepository.findAllByReceiverUsernameOrderByCreatedAtDesc(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
