package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.entity.Notification;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.respository.NotificationRepository;
import com.doctorcare.PD_project.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
    private final UserRepository userRepository;

    public Mono<Void> pushNotification(NotificationMessage notification) {
        return sseService.sendToUser(notification.getUsername(), notification);
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    // Lấy userId từ JWT trong service
    private String getCurrentUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getClaim("id");
    }

    public Page<Notification> getPageNotification(Pageable pageable) throws AppException {
        String userId = getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));
        return notificationRepository.findAllByReceiverOrderByCreatedAtDesc(user, pageable);
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
