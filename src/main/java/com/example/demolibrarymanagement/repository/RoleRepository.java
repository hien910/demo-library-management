package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleById(Integer id);
    Role findByName(String string);
}
