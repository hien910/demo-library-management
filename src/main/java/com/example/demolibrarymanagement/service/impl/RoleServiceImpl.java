package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.DTO.request.RoleRequest;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Permission;
import com.example.demolibrarymanagement.model.entity.Role;
import com.example.demolibrarymanagement.repository.PermissionRepository;
import com.example.demolibrarymanagement.repository.RoleRepository;
import com.example.demolibrarymanagement.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(RoleRequest request) {
        Set<Integer> idPermission = new HashSet<>(request.getAddPermissionId());
        Collections.addAll(idPermission,1,5,6);
        List<Permission> permissionList = permissionRepository.findAllById(idPermission);
        Role role = Role.builder()
                .name(request.getName())
                .permissions(permissionList)
                .build();
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(RoleRequest request, Integer id) throws DataNotFoundException {
        Role role = roleRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find role has: "+ id));
        List<Permission> permissionList = permissionRepository.findAllById(request.getAddPermissionId());
        role.setPermissions(permissionList);
        role.setName(request.getName());
        return roleRepository.save(role);
    }

    @Override
    public Role deleteRole(Integer id) throws DataNotFoundException {
        Role role = roleRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Cannot find role has: "+ id));
        roleRepository.delete(role);
        return role;
    }
}
