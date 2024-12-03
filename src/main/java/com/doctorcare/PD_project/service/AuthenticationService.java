package com.doctorcare.PD_project.service;

import com.doctorcare.PD_project.dto.request.AuthenticationRequest;
import com.doctorcare.PD_project.dto.request.ExchangeTokenRequest;
import com.doctorcare.PD_project.dto.request.IntrospectRequest;
import com.doctorcare.PD_project.dto.response.*;
import com.doctorcare.PD_project.entity.Patient;
import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.enums.ErrorCode;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.event.create.OnRegisterEvent;
import com.doctorcare.PD_project.exception.AppException;
import com.doctorcare.PD_project.mapping.UserMapper;
import com.doctorcare.PD_project.responsitory.OnboardUserClient;
import com.doctorcare.PD_project.responsitory.OutboundClient;
import com.doctorcare.PD_project.responsitory.PatientRepository;
import com.doctorcare.PD_project.responsitory.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    OutboundClient outboundClient;
    OnboardUserClient onboardUserClient;
    PatientService patientService;
    UserMapper userMapper;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    @Value("${outbound.identity.grant-type}")
    protected String GRANT_TYPE;

    @NonFinal
    @Value("${token.accessToken}")
    protected int accessDuration;

    @NonFinal
    @Value("${token.refreshToken}")
    protected int refreshDuration;

    private final PatientRepository patientRepository;
    ApplicationEventPublisher applicationEventPublisher;
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AppException {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND));

        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPwd());
        if (!authenticate)
            throw new AppException(ErrorCode.INVALID_CREDENTIAL);
        if (!user.isEnable() ) {
            applicationEventPublisher.publishEvent(new OnRegisterEvent(user, "http://localhost:8082/api/v1/patient", Locale.ENGLISH));
            throw new AppException(ErrorCode.NO_ACTIVE);
        }
        var accessToken = generateAccess(user);
        var refreshToken = generateRefresh(user.getId());
        return AuthenticationResponse.builder()
                .userResponse(userMapper.toUserResponse(user))
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .roles(user.getRole())
                .build();
    }

    private String generateRefresh(String id) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(id)
                .issuer("tuandat.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(refreshDuration, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return  jwsObject.serialize(); // hiển thị thông tin tuần tự header.payload.signature
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }

    public RefreshTokenResponse getAccessToken(IntrospectRequest request) throws JOSEException, ParseException, AppException {
        String token = request.getRefreshToken();

        IntrospectResponse valid = introspect(request);
        if(!valid.isValid()){
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }
        JWTClaimsSet extract = extractToken(token);
        User user = userRepository.findById(extract.getSubject()).get();
        String accessToken = generateAccess(user);
        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .build();
    }

    public AuthenticationResponse outBoundAuthenticate(String code) throws AppException {
        System.out.println(CLIENT_ID);
        ExchangeTokenResponse exchangeTokenResponse =  outboundClient.exchangeToken(ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(CLIENT_ID)
                        .clientSecret(CLIENT_SECRET)
                        .grantType(GRANT_TYPE)
                        .redirectUri(REDIRECT_URI)
                        .build());
        UserGoogleResponse userGoogleResponse = onboardUserClient.getUserInfo("json",exchangeTokenResponse.getAccessToken());
        Patient patient = userMapper.toPatient(userGoogleResponse);
        patient.setUsername(userGoogleResponse.getEmail());
        patient.setRole(Roles.PATIENT.name());
        patient.setEnable(true);
        patientRepository.findByUsername(patient.getUsername()).orElseGet(() -> {
                    System.out.println("Da luu patient");
                    return patientRepository.save(patient);
                }
        );
        System.out.println(patient.toString());
        UserResponse userResponse = userMapper.toUserResponse(patient);
        var accessToken = generateAccess(patient);
        var refreshToken = generateRefresh(patient.getId());
        System.out.println(accessToken);
        return AuthenticationResponse.builder()
                .userResponse(userResponse)
                .refreshToken(refreshToken)
                .accessToken(accessToken).build();
    }

    private String generateAccess(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("sohan.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(accessDuration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("roles", user.getRole())
                .claim("id", user.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return  jwsObject.serialize(); // hiển thị thông tin tuần tự header.payload.signature
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }


    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getRefreshToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(verifier);

        Date expiryTime = extractToken(token).getExpirationTime();

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date()))
                .build();
    }

    public JWTClaimsSet extractToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet();
    }


}
