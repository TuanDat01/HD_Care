package com.doctorcare.PD_project.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String signerKey;

    private static final String[] PUBLIC_URL_POST = {
            "/auth/**",
            "/auth/refreshToken",
            "/patient"
    };
    private static final String[] PUBLIC_URL_GET = {
            "/appointment/pdf2/",
            "/auth/**",
            "/patient/verify",
            "/appointment/*",
            "/appointment",
            "/doctor/*",
            "/doctor",
            "/auth/verify",
            "/doctor-schedule",
            "/doctor/**",

    };

    private static final String[] DOCTOR_URL = {
            "/doctor-schedule/*",
            "/medicine/*",
            "/prescription/*",
            "/appointment/doctor-appointment/*",
            "/appointment/doctor-appointments"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> {
            try {
                request
                        .requestMatchers(DOCTOR_URL).hasRole("DOCTOR")
                        .requestMatchers(HttpMethod.POST,"/doctor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/doctor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, PUBLIC_URL_POST).permitAll()
                        .requestMatchers(HttpMethod.GET,PUBLIC_URL_GET).permitAll()
                        .anyRequest().authenticated()
                ;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        httpSecurity.cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwtConfigurer -> jwtConfigurer
                            .decoder(jwtDecoder()) // Để cho phép truy cập khi cung cấp đúng token
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                            .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
//                        .accessDeniedHandler(new customerHandle())
        );

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        // Tạo 1 key để mã hóa -> cung cấp signerKey và thuật toán
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
