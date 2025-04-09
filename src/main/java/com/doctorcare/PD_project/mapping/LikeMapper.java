package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.response.LikeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    LikeResponse toLikeResponse(String likeId, String userId, String postId);
}
