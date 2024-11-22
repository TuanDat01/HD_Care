package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.UpdateDoctorRequest;
import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.DoctorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class DoctorController {
    DoctorService doctorService;

    @PostMapping
    public ApiResponse<UserResponse> CreateDoctor(@Valid @RequestBody CreateUserRequest userRequest) throws AppException {
        return ApiResponse.<UserResponse>builder().result(doctorService.CreateDoctor(userRequest)).build();
    }
    @GetMapping("/{id}")
    public ApiResponse<DoctorResponse> FindDoctor(@PathVariable String id) throws AppException {
        return ApiResponse.<DoctorResponse>builder().result(doctorService.FindDoctorResponseById(id)).build();
    }
    @PutMapping("/{id}")
    public ApiResponse<DoctorResponse> UpdateInfo(@PathVariable String id, @RequestBody UpdateDoctorRequest doctorRequest) throws AppException {
        return ApiResponse.<DoctorResponse>builder().result(doctorService.UpdateInfo(id, doctorRequest)).build();
    }


    @GetMapping
    public ApiResponse<PageResponse> getAll(@RequestParam(name = "name", required = false) String name,
                                                    @RequestParam(name = "district", required = false) String district,
                                                    @RequestParam(name = "city",required = false) String city,
                                                    @RequestParam(name = "page",required = false) int page) throws AppException {
       return ApiResponse.<PageResponse>builder().result(doctorService.GetAll(district, name, city,page)).build();
    }

    @GetMapping("{id}/review")
    public ApiResponse<ReviewResponse> getListReview(@PathVariable String id) throws AppException {
        Doctor doctor = doctorService.findDoctorById(id);
        ReviewResponse reviewResponse = new ReviewResponse();
        List<Review> reviewList = doctor.getReviews();
        reviewResponse.setReviewList(reviewList);
        reviewResponse.setCountReview(reviewList);
        reviewResponse.setCountAvg(reviewList);
        return ApiResponse.<ReviewResponse>builder().result(reviewResponse).build();
    }



}