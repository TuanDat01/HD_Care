package com.doctorcare.PD_project.configure;

import com.doctorcare.PD_project.entity.User;
import com.doctorcare.PD_project.enums.Roles;
import com.doctorcare.PD_project.responsitory.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                List<String> roles = new ArrayList<>();
                roles.add(Roles.ADMIN.name());

                User user = new User();
                user.setUsername("admin");
                user.setRoles(roles);
                user.setPwd(passwordEncoder.encode("admin"));
                userRepository.save(user);
                userRepository.save(user);
                log.warn("Admin user has been created");
            }
        };
    }
}
