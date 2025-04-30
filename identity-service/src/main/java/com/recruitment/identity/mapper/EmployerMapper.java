package com.recruitment.identity.mapper;

import com.recruitment.identity.dto.request.EmployerCreationRequest;
import org.mapstruct.Mapper;

import com.recruitment.identity.dto.request.UserCreationRequest;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployerMapper {
    @Mapping(target = "userId", ignore = true)
    EmployerCreationRequest toEmployerCreationRequest(UserCreationRequest userCreationRequest);
}
