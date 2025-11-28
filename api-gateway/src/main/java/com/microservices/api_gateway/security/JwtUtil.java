package com.microservices.api_gateway.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET = "your-256-bit-secret-your-256-bit-secret"; // 32+ chars

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    public String getUsername(String token) {
        return getAllClaims(token).getPayload().getSubject();
    }

    public String getRole(String token) {
        return getAllClaims(token).getPayload().get("role", String.class);
    }

    private Jws<Claims> getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
    }

}
