package com.gotrack.api_gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.cloud.gateway.server.mvc.filter.FormFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gotrack.api_gateway.config.JwtKeyConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class GlobalFilter extends OncePerRequestFilter implements Ordered {
    private final long EXPIRATION = 5 * 60 * 1000;
    private final JwtKeyConfig jwtKeyConfig;

    public GlobalFilter(JwtKeyConfig jwtKeyConfig) {
        this.jwtKeyConfig = jwtKeyConfig;
    }

    @Override
    public int getOrder() {
        return FormFilter.FORM_FILTER_ORDER - 1;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Missing or invalid Authorization header\"}");
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("Global Filter: processing request for " + path);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtKeyConfig.getClientPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            String requestId = UUID.randomUUID().toString();

            String internalToken = Jwts.builder()
                    .setSubject(email)
                    .claim("role", role)
                    .claim("requestId", requestId)
                    .setIssuedAt(new java.util.Date())
                    .setExpiration(new java.util.Date(System.currentTimeMillis() + EXPIRATION))
                    .signWith(jwtKeyConfig.getGatewayPrivateKey())
                    .compact();

            HttpServletRequest wrapped = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    return switch (name.toLowerCase()) {
                        case "x-internal-token" -> internalToken;
                        case "x-email" -> email;
                        case "x-user-role" -> role;
                        case "x-request-id" -> requestId;
                        default -> super.getHeader(name);
                    };
                }

                @Override
                public Enumeration<String> getHeaders(String name) {
                    String val = getHeader(name);
                    if (val != null
                            && (name.toLowerCase().startsWith("x-") || name.equalsIgnoreCase("Authorization"))) {
                        return Collections.enumeration(List.of(val));
                    }
                    return super.getHeaders(name);
                }

                @Override
                public Enumeration<String> getHeaderNames() {
                    Set<String> names = new HashSet<>();
                    Collections.list(super.getHeaderNames()).forEach(n -> names.add(n.toLowerCase()));
                    names.addAll(List.of("x-internal-token", "x-email", "x-user-role", "x-request-id"));
                    return Collections.enumeration(names);
                }
            };

            filterChain.doFilter(wrapped, response);

        } catch (JwtException e) {
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter()
                    .write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Invalid or expired token\"}");
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"\"}");
        }

    }
}
