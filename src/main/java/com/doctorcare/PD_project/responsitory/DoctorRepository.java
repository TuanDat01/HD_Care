package com.doctorcare.PD_project.responsitory;

import com.doctorcare.PD_project.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,String> {
    List<Doctor> findByLocation(String location);
    List<Doctor> findByNameContaining(String name);

    List<Doctor> findAllByOrderByPriceAsc();

    List<Doctor> findAllByOrderByPriceDesc();

}
