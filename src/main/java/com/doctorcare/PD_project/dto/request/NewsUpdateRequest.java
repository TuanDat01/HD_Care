package com.doctorcare.PD_project.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NewsUpdateRequest {
    private String title;
    private String content;
    private String category;
    private String coverImageUrl;

    @Builder.Default
    private boolean isDraft = false;
}
