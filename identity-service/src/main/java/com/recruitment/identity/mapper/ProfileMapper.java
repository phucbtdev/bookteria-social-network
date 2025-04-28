package com.recruitment.identity.mapper;

import org.mapstruct.Mapper;

import com.recruitment.identity.dto.request.ProfileCreationRequest;
import com.recruitment.identity.dto.request.UserCreationRequest;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "userId", ignore = true)
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest userCreationRequest);
}
