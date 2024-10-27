package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.dto.request.DoctorScheduleRequest;
import com.doctorcare.PD_project.dto.response.FindScheduleResponse;
import com.doctorcare.PD_project.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    @Query("select new com.doctorcare.PD_project.dto.request.DoctorScheduleRequest(s.id, d.name, u.img, u.email, s.start, s.end) " +
            "from Doctor d " +
            "join User u on u.id = d.id " +
            "join d.schedules s " +
            "where d.id = :doctorId and s.id = :scheduleId")
    DoctorScheduleRequest getInfoSchedule(@Param("doctorId") String doctorId, @Param("scheduleId") String scheduleId);

    @Query(value = "select * from schedule where schedule.doctor_id = :doctorId",nativeQuery = true)
    List<Schedule> findSchedule(@Param("doctorId") String doctorId);




}