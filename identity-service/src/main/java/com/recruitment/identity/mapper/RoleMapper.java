package com.recruitment.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.recruitment.identity.dto.request.RoleRequest;
import com.recruitment.identity.dto.response.RoleResponse;
import com.recruitment.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
