package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("SELECT COUNT(p) FROM Patient p WHERE p.createdAt >= :startOfDay AND p.createdAt < :endOfDay")
    long countNewPatients(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.createdAt >= :startOfDay AND d.createdAt < :endOfDay")
    long countNewDoctors(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
