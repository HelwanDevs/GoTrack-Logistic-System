package com.gotrack.auth_service.Jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email = null;
        try {
            Claims claims = jwtService.extractAllClaims(token);
            System.out.println("JwtFilter is processing token: " + token);
            email = claims.getSubject();
            Object accountId = claims.get("accountId");

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("Filter is looking for user: [" + email + "]");

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if (!jwtService.isTokenValid(token, userDetails.getUsername())) {
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                if (!userDetails.isEnabled()) {
                    sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                            "This account is disabled or deleted");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(accountId);

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (UsernameNotFoundException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User no longer exists");
            return;
        } catch (ExpiredJwtException e) {
            String path = request.getServletPath();
            if (path.equals("/api/auth/refresh-token")) {
                filterChain.doFilter(request, response);
            } else
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");

            return;
        } catch (JwtException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid, tampered, or expired token");
            return;
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "An error occurred during authentication");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = String.format("{\"status\": %d, \"error\": \"Authentication Error\", \"message\": \"%s\"}",
                status, message);
        response.getWriter().write(json);
    }
}