package com.recruitment.identity.service;

import java.util.List;

import com.recruitment.identity.entity.Permissions;
import org.springframework.stereotype.Service;

import com.recruitment.identity.dto.request.PermissionRequest;
import com.recruitment.identity.dto.response.PermissionResponse;
import com.recruitment.identity.mapper.PermissionMapper;
import com.recruitment.identity.repository.PermissionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permissions permissions = permissionMapper.toPermission(request);
        permissions = permissionRepository.save(permissions);
        return permissionMapper.toPermissionResponse(permissions);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission) {
        permissionRepository.deleteById(permission);
    }
}
