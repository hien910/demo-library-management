package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.DTO.request.UpsertPermissionRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Permission;
import com.example.demolibrarymanagement.repository.PermissionRepository;
import com.example.demolibrarymanagement.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IPermissionImpl implements IPermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public Permission createPermission(UpsertPermissionRequest request) {
        Permission permission = Permission.builder()
                .url(request.getUrl())
                .build();
        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(Integer id, UpsertPermissionRequest request) throws DataNotFoundException {
        Permission permission = permissionRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find permission with id: " + id));
        permission.setUrl(request.getUrl());

        permissionRepository.save(permission);
        return null;
    }

    @Override
    public Permission deletePermission(Integer id) throws DataNotFoundException {
        Permission permission = permissionRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find permission with id: " + id));
        permissionRepository.delete(permission);
        return permission;
    }

}
