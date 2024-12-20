package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.dto.request.AppointmentRequest;
import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.dto.response.ManagePatient;
import com.doctorcare.PD_project.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "and (:status is null or a.status = :status)" +
            " order by s.start desc ")
    Page<Appointment> findAllByPatientId(@Param("idPatient") String id,
                                         @Param("startDate") String startDate,
                                         @Param("endDate") String endDate,
                                         @Param("status") String status,
                                         Pageable pageable);

    @Query("select a from Appointment a" +
            " join a.doctor d " +
            "join a.schedule s" +
            " where d.id = :doctorId and" +
            " (:date IS NULL or FUNCTION('DATE', s.end) = :date)" +
            " and (:status is null or a.status=:status)" +
            " and (:name is null or a.patient.name like %:name%) order by HOUR(s.start) desc")
    Page<Appointment> findByDoctor(@Param("doctorId") String doctorId,
                                         @Param("date") String date,
                                         @Param("status") String status,
                                         @Param("name") String name,
                                         Pageable pageable);

    Appointment findAllByDoctorId(String doctorId);

    @Query("select a from Appointment a " +
            "join a.schedule s " +
            "join a.doctor d " +
            "where (:startDate is null and :endDate is null or FUNCTION('DATE', s.start) " +
            "between FUNCTION('DATE', :startDate) and FUNCTION('DATE', :endDate)) " +
            "and d.id = :doctorId " +
            "and (:status is null or a.status = :status) " +
            "and (:name is null or a.patient.name like %:name%) " +
            "order by DATEDIFF(FUNCTION('DATE', s.start), CURRENT_DATE) desc ")
    Page<Appointment> filterAppointment(
            @Param("doctorId") String id,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("status") String status,
            @Param("name") String name,
            Pageable pageable
    );




    List<Appointment> findAppointmentBySchedule(Schedule schedule);

    Appointment findAppointmentByPrescription(Prescription prescription);
    @Query("SELECT new com.doctorcare.PD_project.dto.response.ManagePatient(" +
            "p.id, p.name, p.dob, MAX(a.schedule.end), " +
            "p.address, p.email, p.phone, COUNT(p.id)) " +
            "FROM Appointment a " +
            "JOIN a.patient p " +
            "WHERE a.doctor.id = :doctorId " +
            "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "GROUP BY p.id, p.name, p.dob, p.address, p.email, p.phone " +
            "ORDER BY MAX(a.schedule.end) DESC")
    Page<ManagePatient> getPatientOfDoctor(@Param("doctorId") String id,
                                           @Param("keyword") String keyword,
                                           Pageable pageable);


    @Override
    long count();

    //    @Query("select a from Appointment a where" +
//            " a.doctor = :doctor and" +
//            " a.status = :status")
//    List<Appointment> findAppointmentByStatus(@Param("status") String status, @Param("doctor")Doctor doctor);
}
