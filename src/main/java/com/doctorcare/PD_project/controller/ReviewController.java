package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreateReview;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.ReviewResponse;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.ReviewService;
import com.itextpdf.text.pdf.qrcode.Mode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("/review")
@Controller
public class ReviewController {
    private ReviewService reviewService;
    @GetMapping
    public String displayReviewPage(Model theModel, @RequestParam(value = "idPatient") String idPatient,
                                    @RequestParam(value = "idDoctor") String idDoctor) {
        theModel.addAttribute("reviewIn", new CreateReview());
        return "index";
    }

    @PostMapping
    public String uploadImage(@ModelAttribute("reviewIn") CreateReview createReview,
                                                 @RequestParam(value = "image",required = false) List<MultipartFile> file2,
                                                 @RequestParam(value = "idPatient") String idPatient,
                                                 @RequestParam(value = "idDoctor") String idDoctor,
                                                 Model model) throws IOException, AppException, ExecutionException, InterruptedException {
       model.addAttribute("review", reviewService.create(createReview, file2, idPatient, idDoctor));
       return "a";
    }
//    @PutMapping("/{id}")
//    public ApiResponse<CreateReview> updateReview(@PathVariable String id,@RequestParam CreateReview createReview){
//        return ApiResponse.<CreateReview>builder().result(reviewService.updateReview(id,createReview)).build();
//    }
}
