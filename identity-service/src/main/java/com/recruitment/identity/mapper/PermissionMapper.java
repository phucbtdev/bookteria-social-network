package com.recruitment.identity.mapper;

import org.mapstruct.Mapper;

import com.recruitment.identity.dto.request.PermissionRequest;
import com.recruitment.identity.dto.response.PermissionResponse;
import com.recruitment.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
