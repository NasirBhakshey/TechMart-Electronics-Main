package com.microservices.user_service.Security;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private static final String SECRET_KEY = "mySuperSecretKey12345mySuperSecretKey12345"; // should be at least 256
                                                                                           // bits (32 chars)

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateAccessToken(String username, List<String> role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)    
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 minutes
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey()) // new method instead of setSigningKey()
                    .build()
                    .parseSignedClaims(token); // will throw if invalid
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token) {
        return (String) Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
