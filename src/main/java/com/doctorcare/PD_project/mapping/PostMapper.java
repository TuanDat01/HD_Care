package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.CreatePostRequest;
import com.doctorcare.PD_project.dto.response.PostResponse;
import com.doctorcare.PD_project.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = {
                java.util.Collections.class,
                java.util.stream.Collectors.class
        }
)
public interface PostMapper {
    @Mapping(target = "imageUrls",
            expression = "java(post.getImages() == null ? "
                    + "Collections.emptyList() : "
                    + "post.getImages().stream()"
                    + ".map(PostImage::getImageUrl)"
                    + ".collect(Collectors.toList()))")
    PostResponse toPostResponse(Post post);

    Post toPost(CreatePostRequest postRequest);
}
