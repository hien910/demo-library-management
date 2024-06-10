package com.example.demolibrarymanagement.controller;
import com.example.demolibrarymanagement.DTO.request.LoginRequest;
import com.example.demolibrarymanagement.DTO.request.RegisterRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertBook;
import com.example.demolibrarymanagement.DTO.request.UpsertUser;
import com.example.demolibrarymanagement.DTO.response.Response;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.Book;
import com.example.demolibrarymanagement.model.entity.User;
import com.example.demolibrarymanagement.security.JwtUtil;
import com.example.demolibrarymanagement.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Response<String>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request)
            throws DataNotFoundException, AccountLockedException {
        String token = userService.login(loginRequest, request);
        return ResponseEntity.ok().body(
                new Response<>("200", "Login successfully", token)
        );
    }
    @PostMapping("/register")
    public ResponseEntity<Response<User>> register(@RequestBody RegisterRequest request){
        try{
            User user = userService.register(request);
            return ResponseEntity.ok().body(
                    new Response<>("200", "Register successfully", user)
            );
        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    new Response<>("500", e.getMessage())
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response<User>> updateUser(@RequestBody UpsertUser upsertUser, @PathVariable Integer id) {
        try {
            User user = userService.updateUser(upsertUser, id);
            Response<User> response = new Response<>("200", "User updated successfully", user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataNotFoundException e) {
            Response<User> response = new Response<>("404", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
