package com.doctorcare.PD_project.dto.request;

import lombok.Data;

@Data
public class NewsUpdateRequest {
    private String title;
    private String content;
    private String category;
    private String coverImageUrl;
    private boolean isDraft;
}
