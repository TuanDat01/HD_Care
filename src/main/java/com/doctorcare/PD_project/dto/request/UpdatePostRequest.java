package com.doctorcare.PD_project.dto.request;

import com.doctorcare.PD_project.entity.PostImage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UpdatePostRequest {
    private String content;
    private String doctorId;
    private List<PostImage> images;

    @Builder.Default
    private boolean isHidden = false;
}
