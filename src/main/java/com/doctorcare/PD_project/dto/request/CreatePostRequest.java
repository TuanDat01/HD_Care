package com.doctorcare.PD_project.dto.request;

import com.doctorcare.PD_project.entity.PostImage;
import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequest {
    private String content;
    private List<PostImage> images;
    private boolean isHidden;
}
