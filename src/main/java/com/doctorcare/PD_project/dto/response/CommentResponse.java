package com.doctorcare.PD_project.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {
    private String id;
    private String content;
    private LocalDateTime createdAt;
    private BasicInfoUserResponse user;
}
