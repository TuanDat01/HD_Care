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
    USERNAME_EXISTS(1010, "Username existed", HttpStatus.BAD_REQUEST),
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
    NOT_FOUND_PRESCRIPTION(1024, "not found prescription",HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(1025, "Email existed", HttpStatus.CONFLICT),
    INVALID_FORMAT(1030, "Email Invalid email format", HttpStatus.CONFLICT),
    TOKEN_EXPIRED(1026, "Token expired", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1033, "you do not permission to access", HttpStatus.FORBIDDEN),
    PASSWORD_EXIST(1027, "password is exist", HttpStatus.BAD_REQUEST),
    NO_ACTIVE(1028,"Please check email to active" ,HttpStatus.OK),
    UPDATE_STATUS(1029, "Please update status is confirmed to prescription", HttpStatus.BAD_REQUEST),
        PAGE_VALID(1030,"input page invalid" ,HttpStatus.BAD_REQUEST ),
        START_TIME_EXISTED(1031,"Start Time existed" , HttpStatus.BAD_REQUEST),
    DURATION_TIME(1032,"duration start between end must be one hours",HttpStatus.BAD_REQUEST);


    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    private final int code;
    private final String message;
    private final HttpStatus status;
}
