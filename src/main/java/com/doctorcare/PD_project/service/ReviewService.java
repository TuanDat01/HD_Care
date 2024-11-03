package com.doctorcare.PD_project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.doctorcare.PD_project.dto.request.CreateReview;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.mapping.ReviewMapper;
import com.doctorcare.PD_project.responsitory.ReviewReponsitory;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ReviewService {
    ReviewReponsitory reviewReponsitory;
    ReviewMapper reviewMapper;

    public CreateReview create(CreateReview createReview, List<MultipartFile> file2) throws IOException {
       Review review =  reviewMapper.toReview(createReview);
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        StringBuilder imageUrls = new StringBuilder();
        for (int i = 0;i<file2.size();i++) {
            imageUrls.append(LocalDateTime.now().toString());
            imageUrls.append(i);
            Map params1 = ObjectUtils.asMap(
                    "public_id" ,imageUrls.toString(),
                    "use_filename", true,   // Automatically use the original file name
                    "unique_filename", false
                    // Don’t add a unique suffix to the name
            );
            Map uploadResult = cloudinary.uploader().upload(file2.get(i).getBytes(), params1);
            review.addImg((String) uploadResult.get("secure_url"));
            System.out.println(review.getImg());
        }
        return reviewMapper.toCreateReview(review);
    }
}
