package com.devteria.profile.service;

import com.devteria.profile.dto.request.UserProfileCreation;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.enity.UserProfile;
import com.devteria.profile.mapper.UserProfileMapper;
import com.devteria.profile.repository.UserProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class UserRepositoryService {
    UserProfileRepository userRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(UserProfileCreation request) {
        UserProfile userProfile =  userProfileMapper.toUserProfile(request);
        userProfile = userRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id){
        UserProfile user =  userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userProfileMapper.toUserProfileResponse(user);
    }
}
