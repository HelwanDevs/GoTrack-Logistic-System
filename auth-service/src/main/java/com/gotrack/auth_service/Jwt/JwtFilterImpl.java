package com.gotrack.auth_service.Jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gotrack.auth_service.Exceptions.InvalidTokenException;
import com.gotrack.auth_service.entity.RefreshToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public class JwtFilterImpl implements Filter {

    private final JwtKeyService jwtKeyService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    public JwtFilterImpl(JwtKeyService jwtKeyService, JwtService jwtService, RefreshTokenService refreshTokenService,
            UserDetailsService userDetailsService) {
        this.jwtKeyService = jwtKeyService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();

        // Check for internal token from gateway first
        String internalToken = httpRequest.getHeader("X-Internal-Token");

        String authHeader = httpRequest.getHeader("Authorization");
        if (internalToken != null) {
            try {
                if (!jwtService.validateInternalToken(internalToken)) {
                    sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal token 1");
                    return;
                }

                Claims claims = jwtService.extractInternalClaims(internalToken);
                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                    if (!jwtService.isGatewayTokenValid(internalToken)) {
                        sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                                "Invalid internal token 3");
                        return;
                    }

                    if (!userDetails.isEnabled()) {
                        sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN,
                                "This account is disabled or deleted");
                        return;
                    }

                    RefreshToken userRefreshToken = refreshTokenService.findByUsername(email);
                    if (userRefreshToken == null) {
                        sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                                "No valid refresh token found for user");
                        return;
                    }

                    Set<GrantedAuthority> authorities = new java.util.HashSet<>(
                            userDetails.getAuthorities());
                    if (role != null && !role.isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities);
                    authToken.setDetails(email);

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (JwtException e) {
                sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal token 2");
                return;
            } catch (Exception e) {
                sendErrorResponse(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "An error occurred during authentication");
                return;
            }

            filterChain.doFilter(request, response);
            return;
        }

        // Original client JWT flow
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (authHeader != null && internalToken == null) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized way to access the API");
            return;
        }

        String token = authHeader.substring(7);
        String email = null;
        try {
            Claims claims = jwtService.extractAllClaims(token);
            email = claims.getSubject();
            Object accountId = claims.get("accountId");

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("Filter is looking for user: [" + email + "]");

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if (!jwtService.isTokenValid(token, userDetails.getUsername())) {
                    sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;
                }

                if (!userDetails.isEnabled()) {
                    sendErrorResponse(httpResponse, HttpServletResponse.SC_FORBIDDEN,
                            "This account is disabled or deleted");
                    return;
                }

                RefreshToken userRefreshToken = refreshTokenService.findByUsername(email);
                if (userRefreshToken == null) {
                    sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                            "No valid refresh token found for user");
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
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "User no longer exists");
            return;
        } catch (ExpiredJwtException e) {
            if (path.equals("/api/auth/refresh-token")) {
                filterChain.doFilter(request, response);
            } else
                sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");

            return;
        } catch (JwtException e) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "Invalid, tampered, or expired token");
            return;
        } catch (InvalidTokenException e) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_UNAUTHORIZED,
                    "No valid refresh token found for user");
            return;
        } catch (Exception e) {
            sendErrorResponse(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
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
