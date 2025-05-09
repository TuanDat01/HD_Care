package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.CreateCommentRequest;
import com.doctorcare.PD_project.dto.response.CommentResponse;
import com.doctorcare.PD_project.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CreateCommentRequest commentRequest);
    @Mapping(source = "user",target = "userResponse")
    CommentResponse toCommentResponse(Comment comment);
}
