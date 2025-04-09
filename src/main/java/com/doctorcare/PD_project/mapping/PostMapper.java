package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.CreatePostRequest;
import com.doctorcare.PD_project.dto.response.PostResponse;
import com.doctorcare.PD_project.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toPost(CreatePostRequest postRequest);
    PostResponse toPostResponse(Post post);
}
