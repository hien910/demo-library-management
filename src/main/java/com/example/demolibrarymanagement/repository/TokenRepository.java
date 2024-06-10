package com.example.demolibrarymanagement.repository;

import com.example.demolibrarymanagement.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {
        Token findByToken(String jwtToken);
}
