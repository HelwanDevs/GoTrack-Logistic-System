package com.gotrack.support_and_notifications_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private String secretKeyStr = "thisIsASecretKeyForJWTtokensThatWasMadeByMomoWithAnFathomaleAmountOfHate";

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKeyStr.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public String extractAccountId(String token) {
        return extractAllClaims(token).get("accountId").toString();
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

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}