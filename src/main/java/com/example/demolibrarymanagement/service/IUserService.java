package com.example.demolibrarymanagement.service;

import com.example.demolibrarymanagement.DTO.request.LoginRequest;
import com.example.demolibrarymanagement.DTO.request.RegisterRequest;
import com.example.demolibrarymanagement.DTO.request.UpsertUser;
import com.example.demolibrarymanagement.exception.DataNotFoundException;
import com.example.demolibrarymanagement.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import javax.security.auth.login.AccountLockedException;

public interface IUserService {
    User updateUser(UpsertUser upsertUser, Integer id) throws DataNotFoundException;
    String login(LoginRequest loginRequest, HttpServletRequest request) throws DataNotFoundException, AccountLockedException;

    User register(RegisterRequest request) throws DataNotFoundException;
}
