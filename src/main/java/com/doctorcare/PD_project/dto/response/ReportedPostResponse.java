package com.doctorcare.PD_project.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportedPostResponse {
    String postId;
    String snippet;      // Có thể là một phần nhỏ của content
    LocalDateTime createdAt;
    long newReportCount;
}
