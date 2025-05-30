package com.recruitment.identity.configuration;

import java.util.HashSet;
import java.util.UUID;

import com.recruitment.identity.entity.Roles;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.recruitment.identity.constant.PredefinedRole;
import com.recruitment.identity.entity.Users;
import com.recruitment.identity.repository.RoleRepository;
import com.recruitment.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_EMAIL = "admin@gmail.com";

    @NonFinal
    static final String ADMIN_PASSWORD = "TrongPhuc260@";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
                Roles candidateRoles =roleRepository.save(Roles.builder()
                        .name(PredefinedRole.CANDIDATE_ROLE)
                        .description("Candidate role")
                        .build());

                Roles employerRoles = roleRepository.save(Roles.builder()
                        .name(PredefinedRole.EMPLOYER_ROLE)
                        .description("Employer role")
                        .build());

                Roles adminRoles = roleRepository.save(Roles.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());



                var roleEmployer = new HashSet<Roles>();
                roleEmployer.add(employerRoles);

                Users employer = Users.builder()
                        .id(UUID.fromString("c431828c-668e-4b2d-8f6e-18f31a622bc5"))
                        .email("employer@gmail.com")
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .emailVerified(true)
                        .roles(roleEmployer)
                        .build();

                userRepository.save(employer);

                var roleCandidate = new HashSet<Roles>();
                roleEmployer.add(employerRoles);

                Users candidate = Users.builder()
                        .id(UUID.fromString("c431828c-668e-4b2d-8f6e-18f31a622bc6"))
                        .email("candidate@gmail.com")
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .emailVerified(true)
                        .roles(roleCandidate)
                        .build();

                userRepository.save(candidate);

                var roles = new HashSet<Roles>();
                roles.add(adminRoles);

                Users users = Users.builder()
                        .id(UUID.fromString("c431828c-668e-4b2d-8f6e-18f31a622bc4"))
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .emailVerified(true)
                        .roles(roles)
                        .build();


                userRepository.save(users);
                log.warn("admin users has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
