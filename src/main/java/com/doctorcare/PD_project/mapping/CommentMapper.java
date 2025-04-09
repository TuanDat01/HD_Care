package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.CreateCommentRequest;
import com.doctorcare.PD_project.dto.response.CommentResponse;
import com.doctorcare.PD_project.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CreateCommentRequest commentRequest);
    CommentResponse toCommentResponse(Comment comment);
}
