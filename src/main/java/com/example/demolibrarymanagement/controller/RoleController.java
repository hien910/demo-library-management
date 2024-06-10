package com.example.demolibrarymanagement.controller;

import com.example.demolibrarymanagement.DTO.request.RoleRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertBook;
import com.example.demolibrarymanagement.DTO.response.Response;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.Role;
import com.example.demolibrarymanagement.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
public class RoleController {
    private final IRoleService roleService;

    @GetMapping()
    public ResponseEntity<Response<List<Role>>> getRole(){
        List<Role> roles = roleService.getAllRole();
        return  ResponseEntity.ok().body(new Response<>("200",
                "Get all role successfully", roles));
    }
    @PostMapping("/create")
    public ResponseEntity<Response<Role>> createRole(@RequestBody RoleRequest request){
        Role role = roleService.createRole(request);
        Response<Role> response = new Response<>("201", "Create new role successfully", role);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Response<Role>> updateRole(@RequestBody RoleRequest request, @RequestParam Integer id) {
        try {
            Role role = roleService.updateRole(request, id);
            Response<Role> response = new Response<>("200", "Role updated successfully", role);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            Response<Role> response = new Response<>("404", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response<Role>> deleteRole(@RequestParam Integer id) throws DataNotFoundException {
        Role role = roleService.deleteRole(id);
        Response<Role> response = new Response<>("204", "Role deleted successfully", role);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }


}
