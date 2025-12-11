package com.microservices.api_gateway.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // Public endpoints
        if (path.startsWith("/auth") ||
            path.startsWith("/api/auth") ||
            path.startsWith("/eureka") ||
            path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        // FIRST: Read JWT from cookie
        HttpCookie jwtCookie = exchange.getRequest().getCookies().getFirst("jwt");
        String token = (jwtCookie != null) ? jwtCookie.getValue() : null;

        // SECOND: Read Authorization header if cookie missing
        if (token == null) {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        // If still no token → Unauthorized
        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Token validation
        if (!jwtUtil.validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // Extract role + user details
        String role = jwtUtil.getRole(token);
        String userId = jwtUtil.getUserId(token);
        String email = jwtUtil.getEmail(token);

        // Role-based access control
        if (path.startsWith("/admin") && !"ADMIN".equals(role)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        if (path.startsWith("/manager") && !(List.of("MANAGER", "ADMIN").contains(role))) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }
        if (path.startsWith("/user") && !(List.of("USER", "MANAGER", "ADMIN").contains(role))) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // Add user details to headers (important for microservices)
        ServerWebExchange updatedExchange = exchange.mutate()
                .request(builder -> builder
                        .header("X-USER-ID", userId)
                        .header("X-EMAIL", email)
                        .header("X-ROLE", role)
                )
                .build();

        return chain.filter(updatedExchange);
    }

}
