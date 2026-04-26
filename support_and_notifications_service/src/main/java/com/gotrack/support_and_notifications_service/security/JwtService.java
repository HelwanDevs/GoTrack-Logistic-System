package com.gotrack.support_and_notifications_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private String secretKeyStr = "abcd";

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKeyStr.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public Long extractAccountId(String token) {
        return Long.parseLong(extractAllClaims(token).get("accountId").toString());
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role").toString();
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}