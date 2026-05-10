package com.gotrack.support_and_notifications_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.security.PublicKey;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class InternalTokenFilter extends OncePerRequestFilter {

    private final JwtKeyConfig jwtKeyConfig;

    public InternalTokenFilter(JwtKeyConfig jwtKeyConfig) {
        this.jwtKeyConfig = jwtKeyConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String internalToken = request.getHeader("X-Internal-Token");

        if (internalToken == null) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized Way to Access the API");
            return;
        }

        try {
            PublicKey pubKey = jwtKeyConfig.getGatewayPublicKey();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(pubKey)
                    .build()
                    .parseClaimsJws(internalToken)
                    .getBody();
            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Internal token has expired");
                return;
            }
            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            String accountId = claims.get("accountId", String.class);

            Set<GrantedAuthority> authorities = new HashSet<>();
            if (role != null && !role.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            if (accountId == null || accountId.isEmpty()) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Account ID is missing in the token");
                return;
            } else {
                authorities.add(new SimpleGrantedAuthority("ACCOUNT_" + accountId));
            }

            if (email == null || email.isEmpty()) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Email is missing in the token");
                return;
            } else {
                authorities.add(new SimpleGrantedAuthority("EMAIL_" + email));
            }
            System.out.println("Internal Token Filter: authenticated user " + email + " with role " + role
                    + " and accountId " + accountId);
            UserDetails userDetails = new User(email, "", authorities);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal token");
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Internal token processing error " + e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = String.format("{\"status\": %d, \"error\": \"Unauthorized\", \"message\": \"%s\"}",
                status, message);
        response.getWriter().write(json);
    }
}
