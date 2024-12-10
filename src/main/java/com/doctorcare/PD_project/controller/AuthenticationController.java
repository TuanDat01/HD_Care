package com.doctorcare.PD_project.controller;

import com.doctorcare.PD_project.dto.request.AuthenticationRequest;
import com.doctorcare.PD_project.dto.request.IntrospectRequest;
import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.service.AuthenticationService;
import com.doctorcare.PD_project.service.VerifyTokenService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:3000") // Chỉ cho phép localhost:3000 truy cập

public class AuthenticationController {

    AuthenticationService authenticationService;
    VerifyTokenService verifyTokenService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws AppException {
        AuthenticationResponse result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .message("Login successfully")
                .build();
    }

    @PostMapping("/outbound/authentication")
    ApiResponse<AuthenticationResponse> outBoundAuthenticate(@RequestParam("code") String code) throws AppException {
        System.out.println("Innn");
        var result = authenticationService.outBoundAuthenticate(code);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse <IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/verify")
    public ModelAndView verifyAccount(@RequestParam(value = "token") String token) throws AppException {
        if(token == null) {
            throw new RuntimeException("Token is null");
        }
        verifyTokenService.updateAndDelete(token);
        ModelAndView modelAndView = new ModelAndView("success"); // Tên template Thymeleaf
        modelAndView.addObject("message", "Patient created successfully!");
        return modelAndView;
    }
    @PostMapping("/refreshToken")
    ApiResponse<RefreshTokenResponse> refresh(@RequestBody IntrospectRequest request) throws AppException, ParseException, JOSEException {
        return ApiResponse.<RefreshTokenResponse>builder().result(authenticationService.getAccessToken(request)).build();
    }
}
