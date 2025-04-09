package com.doctorcare.PD_project.dto.request;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String postId;
    private String content;
}
