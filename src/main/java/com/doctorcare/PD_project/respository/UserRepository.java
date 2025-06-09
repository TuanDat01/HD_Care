package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String name);
    Optional<User> findByEmail(String name);

    @Query("SELECT COUNT(p) FROM Patient p WHERE FUNCTION('DATE', p.createdAt) BETWEEN :from AND :to")
    Long countNewPatients(@Param("from") Date from, @Param("to") Date to);

    @Query("SELECT COUNT(d) FROM Doctor d WHERE FUNCTION('DATE', d.createdAt) BETWEEN :from AND :to")
    Long countNewDoctors(@Param("from") Date from, @Param("to") Date to);
}
