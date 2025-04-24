package com.recruitment.identity.mapper;

import com.recruitment.identity.entity.Permissions;
import org.mapstruct.Mapper;

import com.recruitment.identity.dto.request.PermissionRequest;
import com.recruitment.identity.dto.response.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permissions toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permissions permissions);
}
