package com.recruitment.identity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.recruitment.common.dto.request.CandidateCreationRequest;
import com.recruitment.common.dto.request.EmployerCreationRequest;
import com.recruitment.identity.dto.request.CandidateRegisterRequest;
import com.recruitment.identity.dto.request.EmployerRegisterRequest;
import com.recruitment.identity.entity.Roles;
import com.recruitment.identity.entity.Users;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.recruitment.identity.constant.PredefinedRole;
import com.recruitment.identity.dto.request.UserUpdateRequest;
import com.recruitment.identity.dto.response.UserResponse;
import com.recruitment.identity.exception.AppException;
import com.recruitment.identity.exception.ErrorCode;
import com.recruitment.identity.mapper.UserMapper;
import com.recruitment.identity.repository.RoleRepository;
import com.recruitment.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    KafkaTemplate<String,Object> kafkaTemplate;

    public void createAccountCandidate(
            CandidateRegisterRequest request
    ){
        HashSet<Roles> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.CANDIDATE_ROLE).ifPresent(roles::add);

        Users users = Users.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(users);
        log.info("Candidate created successfully: {}", users);

        CandidateCreationRequest creationRequest = CandidateCreationRequest.builder()
                .userId(users.getId())
                .fullName(request.getFullName())
                .currentPackageId(1)
                .build();

        kafkaTemplate.send("candidate-registration", creationRequest);
    }

    public void createAccountEmployer(
            EmployerRegisterRequest request
    ) {
        HashSet<Roles> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.EMPLOYER_ROLE).ifPresent(roles::add);
        Users users = Users.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(users);

        EmployerCreationRequest employerCreationRequest = EmployerCreationRequest.builder()
                .userId(users.getId())
                .fullName(request.getFullName())
                .currentPackageId(1)
                .phone(request.getPhone())
                .companyName(request.getCompanyName())
                .companyCity(request.getCompanyCity())
                .build();
        kafkaTemplate.send("employer-registration", employerCreationRequest);
    }

    @KafkaListener(topics = "employer-creation-success", groupId = "identity-group")
    public void handleEmployerCreationSuccess(
            Map<String, Object> event
    ) {
        UUID userId = UUID.fromString((String) event.get("userId"));
        userRepository.findById(userId).ifPresent(user -> {
            user.setActive(true);
            userRepository.save(user);
        });
    }

    @KafkaListener(topics = "employer-creation-failed", groupId = "identity-group")
    public void handleEmployerCreationFailed(
            Map<String, Object> event
    ) {
        UUID userId = UUID.fromString((String) event.get("userId"));
        userRepository.deleteById(userId);
    }

    @KafkaListener(topics = "candidate-creation-success", groupId = "identity-group")
    public void handleCandidateCreationSuccess(
            Map<String, Object> event
    ) {
        UUID userId = UUID.fromString((String) event.get("userId"));

        userRepository.findById(userId).ifPresent(user -> {
            user.setActive(true);
            userRepository.save(user);
        });
    }

    @KafkaListener(topics = "candidate-creation-failed", groupId = "identity-group")
    public void handleCandidateCreationFailed(
            Map<String, Object> event
    ) {
        UUID userId = UUID.fromString((String) event.get("userId"));
        userRepository.deleteById(userId);
    }

    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userMapper.toUserResponse(
                userRepository.findById(UUID.fromString(authentication.getName()))
                        .orElseThrow(
                                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
                        )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(
            UUID userId, UserUpdateRequest request
    ) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(users, request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        users.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(users));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(
            UUID id
    ) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
