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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandleExeption {
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
@ExceptionHandler(value = AccessDeniedException.class)
ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
    ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

    return ResponseEntity.status(errorCode.getStatus())
            .body(ApiResponse.builder()
                    .code(errorCode.getCode())
                    .message(errorCode.getMessage())
                    .build());
}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            System.out.println(ex.getBindingResult());
            String key = ex.getBindingResult().getAllErrors().stream().findFirst().get().getDefaultMessage();
            errorCode = ErrorCode.valueOf(key);
        } catch (IllegalArgumentException e) {
            System.out.println("kh co enum");
        }
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handle(ConstraintViolationException ex){
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            String key = ex.getConstraintViolations().stream().findFirst().get().getMessageTemplate();
            System.out.println(ex.getConstraintViolations().stream().findFirst().get().getPropertyPath());
            errorCode = ErrorCode.valueOf(key);
        }
        catch (IllegalArgumentException e){
            System.out.println("kh co enum");
        }
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return new ResponseEntity<>(apiResponse,errorCode.getStatus());
    }
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handle(AppException ex) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ex.getErrorCode();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return new ResponseEntity<>(apiResponse, errorCode.getStatus());
    }
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handle(RuntimeException ex) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        System.out.println(ex.getMessage());
        try {
            System.out.println(ex.getMessage());
            errorCode = ErrorCode.valueOf(ex.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("kh co enum");
        }
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return new ResponseEntity<>(apiResponse, errorCode.getStatus());
    }

}
