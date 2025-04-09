package com.doctorcare.PD_project.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowRequestResponse {
    private String id;
    private String followerId;
    private String userId;
    private LocalDateTime createdAt;
}
