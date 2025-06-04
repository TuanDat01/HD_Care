package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.NotificationMessage;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.Notification;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/webhook/receive")
    public Mono<Void> receiveWebhook(@RequestBody NotificationMessage notification) {
        System.out.println("data process" + notification.getUsername());
        return notificationService.pushNotification(notification);
    }

    @GetMapping
    public ApiResponse<Page<Notification>> getAllNotification(@PageableDefault(size = 5,page = 0)Pageable pageable) throws AppException {
        System.out.println(notificationService.getPageNotification(pageable));
        return ApiResponse.<Page<Notification>>builder()
                .message("Get all notification success")
                .result(notificationService.getPageNotification(pageable))
                .build();
    }

    @GetMapping("/get-all")
    public ApiResponse<List<Notification>> getAllNotification() {
        return ApiResponse.<List<Notification>>builder()
                .message("Get all notification success")
                .result(notificationService.getAllNotification())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Notification> updateNotification(@PathVariable String id) throws AppException {
        return ApiResponse.<Notification>builder()
                .message("Update notification success")
                .result(notificationService.updateNotification(id))
                .build();
    }

    @PatchMapping
    public ApiResponse updateAllNotification() {
        notificationService.updateAllNotification();
        return ApiResponse.builder()
                .message("Update all notification success")
                .build();
    }

}
