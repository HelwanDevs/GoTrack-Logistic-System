package com.gotrack.auth_service.Jwt;

import java.util.Date;

import com.gotrack.auth_service.entity.Account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKeyStr;

    public String generateToken(Account account) {
        long EXPIRATION = 15 * 60 * 1000;

        try {
            return Jwts.builder()
                    .setSubject(account.getEmail().trim())
                    .claim("accountId", account.getId().toString())
                    .claim("role", account.getRole())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                    .signWith(Keys.hmacShaKeyFor(secretKeyStr.getBytes()), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Token generation failed: " + e.getMessage());
        }
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKeyStr.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new JwtException("Invalid token");
        }
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
        // }
        // public boolean isTokenExpired(String token) {
        // return extractAllClaims(token).getExpiration().before(new Date());
        // }
    }
}