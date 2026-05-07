package com.gotrack.auth_service.Jwt;

import java.util.Date;

import com.gotrack.auth_service.entity.Account;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Autowired
    private JwtKeyService jwtKeyService;

    public String generateToken(Account account) {
        long EXPIRATION = 15 * 60 * 1000;

        try {
            if (jwtKeyService.isRsaMode()) {
                return Jwts.builder()
                        .setSubject(account.getEmail().trim())
                        .claim("accountId", account.getId().toString())
                        .claim("role", account.getRole())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                        .signWith(jwtKeyService.getPrivateKey())
                        .compact();
            } else {
                return Jwts.builder()
                        .setSubject(account.getEmail().trim())
                        .claim("accountId", account.getId().toString())
                        .claim("role", account.getRole())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                        .signWith(jwtKeyService.getSecretKey())
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
                    .setSigningKey(jwtKeyService.getPublicKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } else {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtKeyService.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
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
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().after(new Date());
    }
}