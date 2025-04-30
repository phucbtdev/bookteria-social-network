package com.recruitment.identity.service;

import java.util.HashSet;
import java.util.List;

import com.recruitment.event.dto.NotificationEvent;
import com.recruitment.identity.entity.Roles;
import com.recruitment.identity.entity.Users;
import com.recruitment.identity.mapper.EmployerMapper;
import com.recruitment.identity.repository.httpclient.EmployerFeignClientRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.recruitment.identity.constant.PredefinedRole;
import com.recruitment.identity.dto.request.UserCreationRequest;
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
    EmployerMapper employerMapper;

    PasswordEncoder passwordEncoder;
    EmployerFeignClientRepository employerFeignClientRepository;

    KafkaTemplate<String,Object> kafkaTemplate;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        Users users = userMapper.toUser(request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Roles> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        users.setRoles(roles);
        users = userRepository.save(users);

        var employer = employerMapper.toEmployerCreationRequest(request);
        employer.setUserId(users.getId());
        employerFeignClientRepository.createEmployer(employer);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("Email")
                .recipient(request.getEmail())
                .subject("Welcome to PhucbtDev")
                .body("Hello" + request.getUsername())
                .build();
        log.info("Users info: {}", users);
        log.info("Notification info: {}",notificationEvent);

        kafkaTemplate.send("notification-delivery1",
                notificationEvent
        );

        return userMapper.toUserResponse(users);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Users users = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        Users users = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(users, request);
        users.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        users.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(users));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
