package com.recruitment.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.recruitment.identity.dto.request.UserCreationRequest;
import com.recruitment.identity.dto.request.UserUpdateRequest;
import com.recruitment.identity.dto.response.UserResponse;
import com.recruitment.identity.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toUser(UserCreationRequest request);

    UserResponse toUserResponse(Users users);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget Users users, UserUpdateRequest request);
}
