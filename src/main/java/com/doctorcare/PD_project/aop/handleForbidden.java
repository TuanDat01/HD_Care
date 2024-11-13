//package com.doctorcare.PD_project.aop;
//
//import com.doctorcare.PD_project.enums.ErrorCode;
//import com.doctorcare.PD_project.exception.AppException;
//import com.doctorcare.PD_project.responsitory.PatientRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.nio.file.AccessDeniedException;
//import java.util.Objects;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
//public class handleForbidden {
//    PatientRepository patientRepository;
//    @Before("execution (* com.doctorcare.PD_project.service.ScheduleService.getScheduleById(..))")
//    public void handle(JoinPoint joinPoint) throws AppException {
//        System.out.println(joinPoint.getArgs()[0]);
//        String currentPatient = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
//        System.out.println(currentPatient);
//        if(!currentPatient.contains("DOCTOR"))
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//
//    }
//}
