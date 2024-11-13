package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.ManagePatient;
import com.doctorcare.PD_project.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    @Query("select a from Appointment a" +
            " join a.patient p" +
            " join a.schedule s" +
            " where p.id = :idPatient " +
            "and (:startDate is null and :endDate is null or FUNCTION('DATE', s.start) between FUNCTION('DATE', :startDate) and FUNCTION('DATE', :endDate)) " +
            " order by s.start desc ")
    List<Appointment> findAllByPatientId(@Param("idPatient") String id,@Param("startDate") String startDate,@Param("endDate") String endDate);
    @Query("select a from Appointment a" +
            " join a.doctor d " +
            "join d.schedules s" +
            " where d.id = :doctorId and" +
            " (:date IS NULL or FUNCTION('DATE', s.end) = :date)" +
            " and a.schedule.id = s.id and (:status is null or a.status=:status)")
    List<Appointment> findByDoctor(@Param("doctorId") String doctorId,@Param("date") String date,@Param("status") String status);
    Appointment findAllByDoctorId(String doctorId);
    @Query("select a from Appointment a " +
            "join a.schedule s " +
            "join a.doctor d " +
            "where (:startDate is null and :endDate is null or FUNCTION('DATE', s.start) between FUNCTION('DATE', :startDate) and FUNCTION('DATE', :endDate)) " +
            "and d.id = :doctorId")
    List<Appointment> filterAppointment(@Param("doctorId") String id, @Param("startDate") String startDate,@Param("endDate") String endDate);

    List<Appointment> findAppointmentBySchedule(Schedule schedule);

    Appointment findAppointmentByPrescription(Prescription prescription);
    @Query("SELECT new com.doctorcare.PD_project.dto.response.ManagePatient(p.id,p.name,p.dob,max(a.schedule.end),p.address,p.email,p.phone,count(p.id)) " +
            "FROM Appointment a join a.patient p WHERE a.doctor.id = :doctorId " +
            "group by p.id" +
            " order by MAX(a.schedule.end) desc")
    List<ManagePatient> getPatientOfDoctor(@Param("doctorId") String id);


//    @Query("select a from Appointment a where" +
//            " a.doctor = :doctor and" +
//            " a.status = :status")
//    List<Appointment> findAppointmentByStatus(@Param("status") String status, @Param("doctor")Doctor doctor);
}
