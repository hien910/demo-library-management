package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Permission findByUrl(String urlName);
    Boolean existsByUrl(String urlName);
}
