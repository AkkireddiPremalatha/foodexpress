package com.menu.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt_secret_key}")
    private String secretKey;

    private Key key;

    // Initialize the key based on the secretKey
    public JwtUtil(@Value("${jwt_secret_key}") String secretKey) {
        this.secretKey = secretKey;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1-hour expiry
            .signWith(key, SignatureAlgorithm.HS256) // Updated method
            .compact();
    }

    public Claims extractClaims(String token) {
        JwtParser parser = Jwts.parserBuilder()
            .setSigningKey(key) // Updated method
            .build();

        return parser.parseClaimsJws(token).getBody();
    }

    public String getEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String getRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token, String email) {
        return email.equals(getEmail(token));
    }
}