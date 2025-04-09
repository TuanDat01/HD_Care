package com.doctorcare.PD_project.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsResponse {
    private String id;
    private String title;
    private String content;
    private UserResponse author;
    private boolean isApproved;
    private UserResponse approvedBy;
    private int usefulCount;
    private int uselessCount;
    private LocalDateTime createdAt;
}
