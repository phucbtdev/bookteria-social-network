package com.devteria.profile.mapper;

import org.mapstruct.Mapper;

import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.enity.UserProfile;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(target = "id", ignore = true)
    UserProfile toUserProfile(ProfileCreationRequest profileCreationRequest);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);

    UserProfileResponse toUserResponse(UserProfile userProfile);
}
