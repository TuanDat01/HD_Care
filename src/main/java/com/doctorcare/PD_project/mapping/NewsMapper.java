package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.NewsCreateRequest;
import com.doctorcare.PD_project.dto.response.NewsResponse;
import com.doctorcare.PD_project.entity.News;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    News toNews(NewsCreateRequest newsCreateRequest);
    NewsResponse toNewsResponse(News news);
}
