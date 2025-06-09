package com.doctorcare.PD_project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteNewsStatsResponse {
    private String newsId;
    private String title;
    private Long favoriteCount;
}