package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.DoctorPageRequest;
import com.doctorcare.PD_project.dto.request.UpdateDoctorRequest;
import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.entity.Review;
import com.doctorcare.PD_project.entity.Schedule;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.event.create.OnRegisterEvent;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.ScheduleMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import com.doctorcare.PD_project.responsitory.ReviewRepository;
import com.doctorcare.PD_project.responsitory.ScheduleRepository;
import com.itextpdf.text.PageSize;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class DoctorService {
    DoctorRepository doctorRepository;
    ReviewRepository reviewRepository;
    UserMapper userMapper;
    ScheduleMapper scheduleMapper;
    ScheduleRepository scheduleRepository;
    PasswordEncoder passwordEncoder;
    ApplicationEventPublisher applicationEventPublisher;
    @Transactional
    public UserResponse CreateDoctor(CreateUserRequest userRequest, HttpServletRequest request) throws AppException {
        if(doctorRepository.findDoctorByUsername(userRequest.getUsername()).isPresent()){
            throw new AppException(ErrorCode.USERNAME_EXISTS);
        }
//        if(doctorRepository.findDoctorByEmail(userRequest.getEmail()).isPresent())
//        {
//            throw new AppException(ErrorCode.USERNAME_EXISTS);
//
//        }
        Doctor doctor = userMapper.toDoctor(userRequest);
        doctor.setRole(Roles.DOCTOR.name());
        doctor.setPwd(passwordEncoder.encode(userRequest.getPassword()));
        Doctor savedDoctor = doctorRepository.save(doctor);

        String appUrl = request.getRequestURL().toString();
        try {
            applicationEventPublisher.publishEvent(new OnRegisterEvent(savedDoctor, appUrl, request.getLocale()));
            throw new AppException(ErrorCode.NO_ACTIVE);

        } catch (RuntimeException exception) {
                System.out.println(exception.getMessage());
            }

        return userMapper.toUserResponse(savedDoctor);
    }
    public List<ScheduleResponse> convertScheduleOfDoctor(Doctor doctor){
        List<ScheduleResponse> scheduleResponses = new ArrayList<>();

        for (Schedule s:doctor.getSchedules()
        ) {
            ScheduleResponse scheduleResponse = scheduleMapper.toScheduleResponse(s);
            scheduleResponse.setDate(LocalDate.now());

            scheduleResponses.add(scheduleResponse);
        }
        return scheduleResponses;
    }
    public List<DoctorResponse> TransformDoctorResponse(List<Doctor> doctors){
        List<DoctorResponse> doctorResponses = doctors.stream().map(doctor -> {
            List<ScheduleResponse> scheduleResponses = convertScheduleOfDoctor(doctor);

            DoctorResponse doctorResponse = userMapper.toDoctorResponse(doctor);
            doctorResponse.setSchedules(scheduleResponses);

            return  doctorResponse;
        }).toList();

        for (int i=0;i<doctorResponses.size();i++){
            Doctor doctor = doctors.get(i);
            DoctorResponse doctorResponse = doctorResponses.get(i);

            doctorResponse.setId(doctor.getId());
        }
        return doctorResponses;
    }
    @Transactional
    public DoctorResponse UpdateInfo(String id, UpdateDoctorRequest doctorRequest) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND_DOCTOR));
        userMapper.updateDoctor(doctorRequest,doctor);
        return userMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    public PageResponse<DoctorResponse> GetAll(String district, String name, String city, int p,String order) throws AppException {
        List<Doctor> doctors = null;
//        DoctorPageRequest doctorPageRequest = new DoctorPageRequest(
//                20,
//                0,
//                order != null ?
//                        ("asc".equalsIgnoreCase(order) ?
//                               Sort.by(Sort.Direction.ASC,"price") : Sort.by(Sort.Direction.DESC, "price"))
//                        : Sort.unsorted() // No sorting if `order` is null
//        );
        Pageable pageable = PageRequest.of(0,20, Sort.by("price").ascending());
        Page<Doctor> doctorPage = doctorRepository.filterDoctor(district,name,city,pageable);

        doctors = doctorPage.getContent().stream().peek(doctor -> {
            List<Schedule> schedule = scheduleRepository.findSchedule(doctor.getId(), LocalDate.now().toString());
            doctor.setSchedules(schedule);
        }).toList();

        List<DoctorResponse> doctorResponse = TransformDoctorResponse(doctors);

        return PageResponse.<DoctorResponse>builder()
                .pageMax(doctorPage.getTotalPages())
                .typeResponse(doctorResponse).build();
    }

    public DoctorResponse FindDoctorResponseById(String id) throws AppException {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));

        List<ScheduleResponse> scheduleResponses = convertScheduleOfDoctor(doctor);

        DoctorResponse doctorResponse = userMapper.toDoctorResponse(doctor);
        doctorResponse.setSchedules(scheduleResponses);

        return  doctorResponse;


    }

    public Doctor findDoctorById(String id) throws AppException {
        return doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));
    }
    public Doctor findDoctorBySchedules(String id){
        return doctorRepository.findDoctorBySchedules(id);
    }

    public void update(Doctor doctor) {

    }

    public UserResponse getDoctor() throws AppException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username" + username);

        Doctor doctor  = doctorRepository.findDoctorByUsername(username).
                orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));

        UserResponse userResponse = userMapper.toUserResponse(doctor);
        userResponse.setNoPassword(!StringUtils.hasText(userResponse.getPassword()));

        return userResponse;
    }

    public ReviewResponse findReviewByDoctorId(String id, String sort, Double star, int page) throws AppException {
        Pageable pageable = PageRequest.of(page-1,3,
        (Objects.equals(sort, "asc") ? Sort.by(Sort.Direction.ASC, "date") : Sort.by(Sort.Direction.DESC, "date")));

        Page<Review> reviews = reviewRepository.findReviewsByDoctorId(id, star,pageable);
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_DOCTOR));

        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setReviewList(reviews.getContent());
        reviewResponse.setPageMax(reviews.getTotalPages());
        reviewResponse.setCountReview(doctor.getNumberOfReviews());
        reviewResponse.setCountAvg(doctor.getAvgRating());

        List<Object[]> list = reviewRepository.countRating();
        reviewResponse.setCountRating(list);

        return reviewResponse;
    }

}
