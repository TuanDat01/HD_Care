package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreateReview;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.ReviewMapper;
import com.doctorcare.PD_project.responsitory.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ReviewService {
    ReviewMapper reviewMapper;
    PatientService patientService;
    DoctorService doctorService;
    SendEmailService emailService;
    ReviewRepository reviewRepository;
    @Transactional
    public CreateReview create(CreateReview createReview, List<MultipartFile> file2, String idPatient, String idDoctor) throws IOException, AppException {
        Review review = reviewMapper.toReview(createReview);
        Doctor doctor = doctorService.findDoctorById(idDoctor);
        Patient patient = patientService.getPatientById(idPatient);

        // Đợi hoàn thành việc upload hình ảnh
        List<String> imgUrls = emailService.sendImage(file2).join();
        review.setImg(imgUrls);  // Đặt danh sách URL ảnh vào review

        // Liên kết `review` với `patient` và `doctor`
        review.setPatient(patient);
        doctor.addReview(review);  // Thêm review vào doctor

        // Lưu `review` vào repository sau khi tất cả đã hoàn tất
        reviewRepository.save(review);

        return reviewMapper.toCreateReview(review);
    }

    public CreateReview updateReview(String id, CreateReview createReview) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay review"));
        CreateReview changeReview = reviewMapper.toCreateReview(review);
        reviewMapper.updateReview(changeReview,createReview);
        return changeReview;
    }
}
