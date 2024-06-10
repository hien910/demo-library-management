package com.example.demolibrarymanagement.controller;
import com.example.demolibrarymanagement.DTO.request.UpsertPermissionRequest;
import com.example.demolibrarymanagement.DTO.response.Response;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.Permission;
import com.example.demolibrarymanagement.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/permission")
public class
PermissionController {
    private final IPermissionService permissionService;

    @PostMapping("/create")
    public ResponseEntity<Response<Permission>> createPermission(@RequestBody UpsertPermissionRequest request){
        Permission permission = permissionService.createPermission(request);
        Response<Permission> response = new Response<>("201", "Permission created successfully", permission);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/update")
    public ResponseEntity<Response<Permission>> updatePermission(@RequestParam Integer id,
                                                                 @RequestBody UpsertPermissionRequest request)
            throws DataNotFoundException {
        Permission permission = permissionService.updatePermission(id, request);
        Response<Permission> response = new Response<>("201", "Permission created successfully", permission);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/insert")
    public ResponseEntity<Response<Permission>> deletePermission(@RequestParam Integer id) throws DataNotFoundException {
        Permission permission = permissionService.deletePermission(id);
        Response<Permission> response = new Response<>("201", "Permission created successfully", permission);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
