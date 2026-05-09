package com.gotrack.auth_service.Jwt;

import java.util.Date;

import com.gotrack.auth_service.entity.Account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtKeyService jwtKeyService;

    public String generateToken(Account account) {
        long EXPIRATION = 60 * 60 * 1000;

        try {
            if (jwtKeyService.isRsaMode()) {
                return Jwts.builder()
                        .setSubject(account.getEmail().trim())
                        .claim("accountId", account.getId().toString())
                        .claim("role", account.getRole())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                        .signWith(jwtKeyService.loadPrivateKey())
                        .compact();
            } else {
                return Jwts.builder()
                        .setSubject(account.getEmail().trim())
                        .claim("accountId", account.getId().toString())
                        .claim("role", account.getRole())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                        .signWith(jwtKeyService.loadSecretKey())
                        .compact();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Token generation failed: " + e.getMessage());
        }
    }

    public Claims extractAllClaims(String token) {
        if (jwtKeyService.isRsaMode()) {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtKeyService.loadPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } else {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtKeyService.loadSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
    }

    public Claims extractAllInternalClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKeyService.loadGatewayPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String email) {
        try {
            Claims claims = extractAllClaims(token);
            String extractedEmail = claims.getSubject();
            Date expiration = claims.getExpiration();
            return extractedEmail.equals(email) && expiration.after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isGatewayTokenValid(String token) {
        try {
            Claims claims = extractAllInternalClaims(token);
            return claims.getExpiration().after(new Date()) && claims.get("role", String.class) != null;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().after(new Date());
    }

    public boolean validateInternalToken(String token) {
        try {
            Claims claims = extractInternalClaims(token);
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
            String role = claims.get("role", String.class);
            return role != null;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims extractInternalClaims(String token) {
        return extractAllInternalClaims(token);
    }

    public String extractRoleFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public String extractRoleFromInternalToken(String token) {
        Claims claims = extractAllInternalClaims(token);
        return claims.get("role", String.class);
    }

    public String extractSubjectFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractUserIdFromInternalToken(String token) {
        Claims claims = extractAllInternalClaims(token);
        return claims.getSubject();
    }
}