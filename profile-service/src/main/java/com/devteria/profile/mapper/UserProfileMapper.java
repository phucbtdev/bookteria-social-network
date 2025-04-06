package com.devteria.profile.mapper;

import com.devteria.profile.dto.request.UserProfileCreation;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.enity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileCreation userProfileCreation);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
