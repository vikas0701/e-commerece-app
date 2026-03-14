package com.ecommerce.userservice.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;
    
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;


    // Generate JWT token
    public String generateToken(String email, String role) {

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(email)   // correct method in 0.11.5
                .claim("role", role)   // Add role claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)  // specify algorithm
                .compact();
    }

    // Extract email from token
    public String extractEmail(String token) {

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String generateRefreshToken() {
        return java.util.UUID.randomUUID().toString();
    }

}