package com.example.demolibrarymanagement.service.impl;

import com.example.demolibrarymanagement.model.entity.Token;
import com.example.demolibrarymanagement.model.entity.User;
import com.example.demolibrarymanagement.repository.TokenRepository;
import com.example.demolibrarymanagement.service.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final TokenRepository tokenRepository;


    @Transactional
    @Override
    public Token addToken(User user, String jwtToken, HttpServletRequest request) {
        Token newToken = Token.builder()
                .token(jwtToken)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusHours(24))
                .build();
        return tokenRepository.save(newToken);
    }


}
