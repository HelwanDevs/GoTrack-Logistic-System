package com.gotrack.support_and_notifications_service.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("TOKEN: " + token);
        System.out.println("TOKEN VALID: " + jwtService.isTokenValid(token));

        if (!jwtService.isTokenValid(token)) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("""
                        {
                          "status": 401,
                          "error": "Unauthorized",
                          "message": "Invalid token"
                        }
                    """);
            return;
        }

        String accountId = jwtService.extractAccountId(token);
        String role = jwtService.extractRole(token);
        String email = jwtService.extractEmail(token);

        String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                accountId, null, List.of(new SimpleGrantedAuthority(authority)));

        authentication.setDetails(email);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }

}
