package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.CreateReview;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.ReviewMapper;
import com.doctorcare.PD_project.responsitory.ReviewRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    AppointmentService appointmentService;
    @Transactional
    public CreateReview create(CreateReview createReview) throws IOException, AppException {
        Review review = reviewMapper.toReview(createReview);

        String id = createReview.getIdAppointment();
        AppointmentRequest appointment = appointmentService.getAppointmentById(id);
        Doctor doctor = doctorService.findDoctorById(appointment.getIdDoctor());
        Patient patient = patientService.getPatientById(appointment.getIdPatient());

        review.setPatient(patient);
        review.setDate(LocalDateTime.now());
        review.setDoctor(doctor);

        doctor.setNumberOfReviews(doctor.getNumberOfReviews() + 1);
        System.out.println(doctor.getNumberOfReviews());
        double calculator = (doctor.getAvgRating() * (doctor.getNumberOfReviews() - 1 ) + createReview.getRating())/doctor.getNumberOfReviews();
        DecimalFormat df = new DecimalFormat("#.##");
        double result = Double.parseDouble(df.format(calculator));
        System.out.println(result);
        doctor.setAvgRating(result);

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
