package com.doctorcare.PD_project.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_KEY(1001, "Invalid key", HttpStatus.BAD_REQUEST),
    FULL_NAME_NOT_BLANK(1002, "Full name cannot be blank", HttpStatus.BAD_REQUEST),
    FULL_NAME_SIZE(1003, "Full name must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1004, "Phone number existed", HttpStatus.CONFLICT),
    PHONE_NUMBER_NOT_BLANK(1005, "Phone number cannot be blank", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_PATTERN(1006, "Invalid phone number", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1007, "Email existed", HttpStatus.CONFLICT),
    EMAIL_NOT_BLANK(1008, "Email cannot be blank", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1009, "Invalid email", HttpStatus.BAD_REQUEST),
    USERNAME_EXISTS(1010, "Username existed", HttpStatus.CONFLICT),
    USERNAME_NOT_BLANK(1011, "Username cannot be blank", HttpStatus.BAD_REQUEST),
    USERNAME_SIZE(1012, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    USERNAME_PATTERN(1013, "Invalid username", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_BLANK(1014, "Password cannot be blank", HttpStatus.BAD_REQUEST),
    PASSWORD_SIZE(1015, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_PATTERN(1016, "Password must contain at least one digit, one lowercase, one uppercase, and one special character", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1017,"No authenticated" , HttpStatus.UNAUTHORIZED );

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private final int code;
    private final String message;
    private final HttpStatus status;
}
