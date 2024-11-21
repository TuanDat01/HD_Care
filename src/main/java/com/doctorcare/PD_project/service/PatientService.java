package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.request.CreatePasswordRequest;
import com.doctorcare.PD_project.dto.request.CreateUserRequest;
import com.doctorcare.PD_project.dto.request.PatientRequest;
import com.doctorcare.PD_project.dto.response.UserResponse;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.event.create.OnRegisterEvent;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mail.EmailService;
import com.doctorcare.PD_project.mapping.AppointmentMapper;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.PatientRepository;
import com.doctorcare.PD_project.responsitory.UserRepository;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Collections;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PatientService {
    ApplicationEventPublisher applicationEventPublisher;
    PatientRepository patientRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    SendEmailService sendEmailService;
    @Transactional
    public void CreatePatient(CreateUserRequest userRequest, HttpServletRequest request) throws AppException {
        if (patientRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTS);
        }

        Patient patient = userMapper.toPatient(userRequest);
        patient.setRole(Roles.PATIENT.name());
        patient.setPwd(passwordEncoder.encode(userRequest.getPassword()));
        Patient patientSaved = patientRepository.save(patient);

        System.out.println(patientSaved);
        if (request != null) {
            String appUrl = request.getRequestURL().toString();

            try {
                applicationEventPublisher.publishEvent(new OnRegisterEvent(patientSaved, appUrl, request.getLocale()));
                throw new AppException(ErrorCode.NO_ACTIVE);
            } catch (RuntimeException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
    public Patient updatePatient(AppointmentRequest appointmentRequest) throws AppException {
        Patient patient = patientRepository.findById(appointmentRequest.getIdPatient()).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND_PATIENT));
        System.out.println(patient);
        PatientRequest updatePatient = userMapper.toPatientRequest(appointmentRequest);
        userMapper.updatePatient(patient,updatePatient);
        System.out.println(patient);
        return patientRepository.save(patient);
    }
    //PostAuthorize("hasAuthority('PATIENT')")
    public PatientRequest updatePatient(PatientRequest patientRequest) throws AppException, IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username" + username);
        Patient patient  = patientRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PATIENT));

        System.out.println("patient: " + patient.toString());
        userMapper.updatePatient(patient, patientRequest);
        System.out.println("patient Update: " + patient.toString());

        return userMapper.toPatientRequest(patientRepository.save(patient));
    }

    public Patient getPatientById(String id) throws AppException {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return patientRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PATIENT));
    }
    public UserResponse getPatient() throws AppException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("username" + username);

        Patient patient  = patientRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PATIENT));

        UserResponse userResponse = userMapper.toUserResponse(patient);
        userResponse.setNoPassword(!StringUtils.hasText(userResponse.getPassword()));

        return userResponse;
    }
    @Transactional
    public void createPassword(CreatePasswordRequest createPasswordRequest) throws AppException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PATIENT));
        if (StringUtils.hasText(patient.getPwd())) {
            throw new AppException(ErrorCode.PASSWORD_EXIST);
        }
        patient.setPwd(passwordEncoder.encode(createPasswordRequest.getPassword()));
    }

    public Patient getPatientByUsername(String username) throws AppException {
        return patientRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_PATIENT));
    }
}
