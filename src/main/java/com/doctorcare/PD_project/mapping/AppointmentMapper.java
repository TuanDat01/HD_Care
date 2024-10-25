package com.doctorcare.PD_project.mapping;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.response.AppointmentResponse;
import com.doctorcare.PD_project.entity.Appointment;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.Schedule;
import org.mapstruct.*;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "patient.name", target = "name")
    @Mapping(source = "patient.address", target = "address")
    @Mapping(source = "patient.id",target = "idPatient")
//    @Mapping(source = "schedule.doctor.name", target = "nameDoctor")
    @Mapping(source = "patient.gender", target = "gender")
    @Mapping(source = "schedule.id", target = "scheduleId")
    @Mapping(source = "patient.email",target = "email")
    AppointmentRequest toAppointmentRequest(Appointment appointment);

    @InheritInverseConfiguration
    Appointment toAppointment(AppointmentRequest appointmentRequest);


}
