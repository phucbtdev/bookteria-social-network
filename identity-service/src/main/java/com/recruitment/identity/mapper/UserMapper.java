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
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "emailVerified",ignore = true)
    @Mapping(target = "roles", ignore = true)
    Users toUser(UserCreationRequest request);

    @Mapping(target = "emailVerified", ignore = true)
    UserResponse toUserResponse(Users users);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "emailVerified", ignore = true)
    void updateUser(@MappingTarget Users users, UserUpdateRequest request);


}
