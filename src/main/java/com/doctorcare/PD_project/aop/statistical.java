package com.doctorcare.PD_project.aop;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import org.aspectj.lang.JoinPoint;
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
    @Before ("execution ( * com.doctorcare.PD_project.service.AppointmentService.createAppointment(..))")
    public void getStatistical(JoinPoint joinPoint){
        AppointmentRequest appointmentRequest = (AppointmentRequest) joinPoint.getArgs()[0];
        countUse.merge(appointmentRequest.getIdDoctor(),1,Integer::sum);
        System.out.println(getDetailStatistical());
    }
    public LinkedHashMap<String,Integer> getDetailStatistical(){
        return countUse.entrySet().stream()
                .sorted(Map.Entry.<String,Integer> comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,LinkedHashMap::new))
                ;
    }
}
