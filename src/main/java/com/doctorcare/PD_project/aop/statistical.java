package com.doctorcare.PD_project.aop;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.entity.Doctor;
import com.doctorcare.PD_project.responsitory.DoctorRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Aspect
@Component
public class statistical {
    @Autowired
    DoctorRepository doctorRepository;

    private final ConcurrentHashMap<String,Integer> countUse = new ConcurrentHashMap<>();
    @AfterReturning(value = "execution(* com.doctorcare.PD_project.service.AppointmentService.createAppointment(..))", returning = "result")
    public void getStatistical(JoinPoint joinPoint, AppointmentRequest result) {
        Doctor doctor = doctorRepository.findById(result.getIdDoctor()).orElseThrow(() ->
                new IllegalArgumentException("Doctor not found for ID: " + result.getIdDoctor())
        );

        countUse.putIfAbsent(result.getIdDoctor(), doctor.getCount() !=0 ? doctor.getCount() : 0);
        countUse.merge(result.getIdDoctor(), 1, Integer::sum);

        doctor.setCount(countUse.get(result.getIdDoctor()));

        doctorRepository.save(doctor); // Lưu vào database
        System.out.println(getDetailStatistical());
    }

    public LinkedHashMap<String,Integer> getDetailStatistical(){
        return countUse.entrySet().stream()
                .sorted(Map.Entry.<String,Integer> comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,LinkedHashMap::new))
                ;
    }
}
