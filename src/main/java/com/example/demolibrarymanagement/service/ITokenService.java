package com.example.demolibrarymanagement.service;

import com.example.demolibrarymanagement.model.entity.Token;
import com.example.demolibrarymanagement.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface ITokenService {
    Token addToken(User user, String token, HttpServletRequest request);
}
