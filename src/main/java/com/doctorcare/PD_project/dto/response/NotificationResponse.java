package com.doctorcare.PD_project.dto.response;

import lombok.Data;

@Data
public class NotificationResponse {
    private String id;
    private String userId;
    private String content;
    private boolean isRead;
}
