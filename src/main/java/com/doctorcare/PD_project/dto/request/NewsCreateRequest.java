package com.doctorcare.PD_project.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class NewsCreateRequest {
    private String title;
    private String content;
    private String category;
    private String coverImageUrl;
    private boolean isDraft = false;
}