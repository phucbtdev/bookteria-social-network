package com.recruitment.identity.service;

import java.util.HashSet;
import java.util.List;

import com.recruitment.event.dto.NotificationEvent;
import com.recruitment.identity.dto.request.EmployerCreationRequest;
import com.recruitment.identity.dto.request.EmployerRegisterRequest;
import com.recruitment.identity.entity.Roles;
import com.recruitment.identity.entity.Users;
import com.recruitment.identity.mapper.EmployerMapper;
import com.recruitment.identity.repository.httpclient.EmployerFeignClientRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    public UserResponse createAccountEmployer(
            EmployerRegisterRequest request
    ) {
        if (userRepository.existsByEmail(request.getEmail())) throw new AppException(ErrorCode.EMAIL_EXISTED);
        Users users = Users.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        HashSet<Roles> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        users.setRoles(roles);
        users = userRepository.save(users);

        EmployerCreationRequest employer = EmployerCreationRequest.builder()
                .userId(users.getId())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .companyName(request.getCompanyName())
                .companyCity(request.getCompanyCity())
                .build();
         log.info("Employer creation request: {}", employer);
        employerFeignClientRepository.createEmployer(employer);

        return userMapper.toUserResponse(users);
    }

    public UserResponse getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userMapper.toUserResponse(
                userRepository.findById(authentication.getName())
                        .orElseThrow(
                                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
                        )
        );
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
