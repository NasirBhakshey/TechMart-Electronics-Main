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

    private static final String SECRET = "your-256-bit-secret-your-256-bit-secret"; // must be 32 bytes

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser() // NEW API
                .verifyWith(getSigningKey()) // Add signing key
                .build().parseSignedClaims(token); // Use parseSignedClaims() instead of parseClaimsJws()
    }

    // ---------------- VALIDATION -----------------
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ Invalid JWT: " + e.getMessage());
            return false;
        }
    }

    // ---------------- CLAIMS -----------------
    private Claims getClaims(String token) {
        return parseToken(token).getPayload();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    public String getUserId(String token) {
        return getClaims(token).get("userId", String.class);
    }

    public String getRole(String token) {
        // role might be saved as "role" or "roles"
        String role = getClaims(token).get("role", String.class);

        if (role == null) {
            // Sometimes roles are stored as array
            Object rolesObj = getClaims(token).get("roles");
            if (rolesObj != null) {
                role = rolesObj.toString().replace("[", "").replace("]", "");
            }
        }

        return role;
    }

}
