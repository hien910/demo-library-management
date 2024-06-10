package com.example.demolibrarymanagement.security;

import com.example.demolibrarymanagement.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${application.security.jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getUsername());
        claims.put("name", user.getName());
        claims.put("role", user.getRole().getName());
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + 24*60*60*1000L))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS512)
                .compact();
    }
    private Key getKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public <T> T getClaim (String token, Function<Claims, T> claimsResolver){
        Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaims(String token){
        Jws<Claims> claims = Jwts
                .parser()
                .setSigningKey(getKey(secretKey))
                .build()
                .parseClaimsJws(token);
        return claims.getBody();
    }
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getClaim(token, Claims::getSubject);
        Date expirationDate = getClaim(token, Claims::getExpiration);
        return !expirationDate.before(new Date()) &&
                username.equals(userDetails.getUsername());
    }

    public String extractSubject(String token){
        return getClaim(token, Claims::getSubject);
    }
}
