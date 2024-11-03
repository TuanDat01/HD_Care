package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.CreateReview;
import com.doctorcare.PD_project.entity.Review;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(CreateReview createReview);
    @InheritInverseConfiguration
    CreateReview toCreateReview(Review review);
}
