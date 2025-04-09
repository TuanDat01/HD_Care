package com.doctorcare.PD_project.respository;

import com.doctorcare.PD_project.entity.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyRepository extends JpaRepository<VerifyToken, String> {
    VerifyToken findByToken(String token);
}
