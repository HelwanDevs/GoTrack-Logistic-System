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

        System.out.println("JWT FILTER CREATED");

        String authHeader = request.getHeader("Authorization");
        System.out.println("AUTH HEADER: " + authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("TOKEN: " + token);
        System.out.println("TOKEN VALID: " + jwtService.isTokenValid(token));

        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accountId = jwtService.extractAccountId(token);
        String role = jwtService.extractRole(token);

        String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                accountId, null, List.of(new SimpleGrantedAuthority(authority)));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("TOKEN VALID: " + jwtService.isTokenValid(token));
        System.out.println("ACCOUNT ID: " + accountId);
        System.out.println("ROLE: " + role);
        System.out.println("AUTHORITY: " + authority);
        System.out.println("SECURITY CONTEXT AUTH: " + SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);

    }

}
