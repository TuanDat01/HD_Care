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
    @Mapping(source = "patient.gender", target = "gender")
    @Mapping(source = "schedule.id", target = "scheduleId")
    @Mapping(source = "patient.email",target = "email")
    @Mapping(source = "schedule.start", target = "start",dateFormat ="dd-MM-yyyy hh:mm" )
    @Mapping(source = "schedule.end", target = "end",dateFormat ="dd-MM-yyyy hh:mm" )
    @Mapping(source = "patient.dob",target = "dob",dateFormat = "dd-MM-yyyy")
    @Mapping(source = "prescription.id", target = "prescriptionId")
    @Mapping(target = "idDoctor",ignore = true)
    @Mapping(target = "nameDoctor", ignore = true)
    @Mapping(target = "result", source = "prescription.result")
    AppointmentRequest toAppointmentRequest(Appointment appointment);

    @InheritInverseConfiguration
    Appointment toAppointment(AppointmentRequest appointmentRequest);


}
