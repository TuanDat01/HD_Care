package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreateReview;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;;
import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Controller
public class UploadImageController {
    private ReviewService reviewService;

    @PostMapping
    public ApiResponse<CreateReview> uploadImage(@RequestParam(value = "review")CreateReview createReview, @RequestParam(value = "image",required = false) List<MultipartFile> file2) throws IOException {
       return ApiResponse.<CreateReview>builder().result(reviewService.create(createReview,file2)).build();
    }
}
