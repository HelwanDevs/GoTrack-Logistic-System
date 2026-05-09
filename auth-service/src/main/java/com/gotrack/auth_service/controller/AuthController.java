package com.gotrack.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.auth_service.Jwt.JwtService;
import com.gotrack.auth_service.Jwt.JwtKeyService;
import com.gotrack.auth_service.dto.AuthResponse;
import com.gotrack.auth_service.dto.LoginRequest;
import com.gotrack.auth_service.dto.LogoutRequest;
import com.gotrack.auth_service.dto.LogoutResponse;
import com.gotrack.auth_service.dto.RefreshTokenRequest;
import com.gotrack.auth_service.dto.RefreshTokenResponse;

import com.gotrack.auth_service.services.AuthService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtService jwtService;

    @Autowired
    JwtKeyService jwtKeyService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.generateNewTokens(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest request) {
        LogoutResponse response = authService.logout(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}