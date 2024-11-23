package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.entity.VerifyToken;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.responsitory.VerifyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class VerifyTokenService {
    VerifyRepository verifyRepository;
    public void saveVerifyToken(VerifyToken verifyToken) {
        System.out.println("da luu");
        verifyRepository.save(verifyToken);
    };
    public VerifyToken findByToken(String token) {
        return verifyRepository.findByToken(token);
    }
    @Transactional
    public User updateAndDelete(String token) throws AppException {  // Cập nhật và xóa VerifyToken
        VerifyToken verifyToken = verifyRepository.findByToken(token);
        if (verifyToken == null) {
            throw new RuntimeException("Token not found");
        }
        if (verifyToken.getExpiryDate().getTime() - System.currentTimeMillis() <= 0) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
        User user = verifyToken.getUser();
        System.out.println("user la:" + user);
        user.setEnable(true);
        System.out.println(user.isEnable());
        verifyRepository.delete(verifyToken);
        return user;
    }
}
