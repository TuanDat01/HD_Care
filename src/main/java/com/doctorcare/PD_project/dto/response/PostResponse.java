package com.doctorcare.PD_project.dto.response;

import com.doctorcare.PD_project.entity.Like;
import com.doctorcare.PD_project.entity.PostImage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostResponse {
    private String id;
    private String content;
    private List<String> imageUrls;
    private boolean isHidden;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int countLikes;
    private int countComments;

    private BasicInfoUserResponse user;
    private BasicInfoUserResponse doctor;
    private boolean liked;
    private boolean saved;
}