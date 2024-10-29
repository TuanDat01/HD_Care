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
    UNAUTHENTICATED(1017,"No authenticated" , HttpStatus.UNAUTHORIZED ),
    DATE_INVALID(1017, "Input schedule no valid",HttpStatus.BAD_REQUEST),
    TIME_INVALID(1018, "start time must before end time",HttpStatus.BAD_REQUEST),
    NOT_FOUND_SCHEDULE(1019, "not found schedule",HttpStatus.NOT_FOUND),
    NOT_FOUND_DOCTOR(1020, "not found doctor",HttpStatus.NOT_FOUND),
    NOT_FOUND_PATIENT(1021, "not found patient",HttpStatus.NOT_FOUND),
    NOT_FOUND_APPOINTMENT(1022, "not found appointment",HttpStatus.NOT_FOUND),
    NOT_FOUND_MEDICINE(1023, "not found medicine",HttpStatus.NOT_FOUND),
    NOT_FOUND_PRESCRIPTION(1024, "not found prescription",HttpStatus.NOT_FOUND)
    ;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private final int code;
    private final String message;
    private final HttpStatus status;
}
