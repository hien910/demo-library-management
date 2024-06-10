package com.example.demolibrarymanagement.service;

import com.example.demolibrarymanagement.DTO.request.RoleRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Role;

import java.util.List;

public interface IRoleService {
    List<Role> getAllRole();

    
    Role createRole(RoleRequest request);
    Role updateRole(RoleRequest request, Integer id) throws DataNotFoundException;

    Role deleteRole(Integer id) throws DataNotFoundException;
}
