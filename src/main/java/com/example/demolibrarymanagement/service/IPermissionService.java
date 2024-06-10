package com.example.demolibrarymanagement.service;

import com.example.demolibrarymanagement.DTO.request.UpsertPermissionRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Permission;

public interface IPermissionService {
    Permission createPermission(UpsertPermissionRequest request);
    Permission updatePermission(Integer id, UpsertPermissionRequest request) throws DataNotFoundException;
    Permission deletePermission(Integer id) throws DataNotFoundException;
}
