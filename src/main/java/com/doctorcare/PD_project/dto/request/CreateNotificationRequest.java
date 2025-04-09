package com.doctorcare.PD_project.dto.request;

import lombok.Data;

@Data
public class CreateNotificationRequest {
    private String userId;
    private String content;
}
