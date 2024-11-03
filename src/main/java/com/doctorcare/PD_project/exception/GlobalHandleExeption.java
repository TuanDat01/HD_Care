package com.doctorcare.PD_project.exception;

import com.doctorcare.PD_project.dto.response.ApiResponse;
import com.doctorcare.PD_project.enums.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandleExeption {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handle(ConstraintViolationException ex){
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        System.out.println(ex.getConstraintViolations().stream().findFirst().get().getMessageTemplate());
        try {
            String key = ex.getConstraintViolations().stream().findFirst().get().getMessageTemplate();
            System.out.println(key);
            errorCode = ErrorCode.valueOf(key);
        }
        catch (IllegalArgumentException e){
            System.out.println("kh co enum");
        }
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return new ResponseEntity<>(apiResponse,errorCode.getStatus());
    }

//    @ExceptionHandler(value = RuntimeException.class)
//    public ResponseEntity<ApiResponse> handle(RuntimeException ex) {
//        ApiResponse apiResponse = new ApiResponse();
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//        System.out.println(ex.getMessage());
//        try {
//            System.out.println(ex.getMessage());
//            errorCode = ErrorCode.valueOf(ex.getMessage());
//        } catch (IllegalArgumentException e) {
//            System.out.println("kh co enum");
//        }
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//        return new ResponseEntity<>(apiResponse, errorCode.getStatus());
//    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handle(AppException ex) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ex.getErrorCode();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return new ResponseEntity<>(apiResponse, errorCode.getStatus());
    }
}
