package com.doctorcare.PD_project.aop;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Aspect
@Component
public class statistical {
    private final ConcurrentHashMap<String,Integer> countUse = new ConcurrentHashMap<>();
    @AfterReturning(value = "execution ( * com.doctorcare.PD_project.service.AppointmentService.createAppointment(..))",returning="result")
    public void getStatistical(JoinPoint joinPoint,AppointmentRequest result){
        AppointmentRequest appointmentRequest = (AppointmentRequest) joinPoint.getArgs()[0];
        countUse.merge(result.getIdDoctor(),1,Integer::sum);
        System.out.println(getDetailStatistical());
    }
    public LinkedHashMap<String,Integer> getDetailStatistical(){
        return countUse.entrySet().stream()
                .sorted(Map.Entry.<String,Integer> comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,LinkedHashMap::new))
                ;
    }
}
