package com.recruitment.identity.mapper;

import org.mapstruct.Mapper;

import com.recruitment.identity.dto.request.ProfileCreationRequest;
import com.recruitment.identity.dto.request.UserCreationRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest userCreationRequest);
}
