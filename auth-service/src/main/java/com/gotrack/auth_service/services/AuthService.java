package com.gotrack.auth_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.gotrack.auth_service.Exceptions.AccountInactiveException;
import com.gotrack.auth_service.Jwt.JwtService;
import com.gotrack.auth_service.Jwt.RefreshTokenService;
import com.gotrack.auth_service.dto.AuthResponse;
import com.gotrack.auth_service.dto.LoginRequest;
import com.gotrack.auth_service.dto.LogoutRequest;
import com.gotrack.auth_service.dto.LogoutResponse;
import com.gotrack.auth_service.dto.RefreshTokenRequest;
import com.gotrack.auth_service.dto.RefreshTokenResponse;
import com.gotrack.auth_service.entity.Account;
import com.gotrack.auth_service.entity.RefreshToken;

import com.gotrack.auth_service.repository.AccountRepository;

@Service
public class AuthService {

    @Autowired
    AccountRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RefreshTokenService refreshTokenService;

    public AuthResponse login(LoginRequest request) {

        Account user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Incorrect email or password"));
        if (user.getDeleted()) {
            throw new AccountInactiveException("Account is inactive or soft-deleted");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (DisabledException e) {
            throw new RuntimeException("Your account is deactivated");
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Incorrect email or password");
        }

        String token = jwtService.generateToken(user);

        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getId(),
                refreshToken);
    }

    public RefreshTokenResponse generateNewTokens(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.validate(request.getRefreshToken());

        Account user = userRepository.findByEmail(refreshToken.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User associated with refresh token not found"));

        String newAccessToken = jwtService.generateToken(user);

        return new RefreshTokenResponse(request.getRefreshToken(), newAccessToken);
    }

    public LogoutResponse logout(LogoutRequest request) {
        try {
            refreshTokenService.deleteByToken(request.getRefreshToken());
        } catch (Exception e) {
            throw new RuntimeException("Failed to invalidate  refresh token: " + e.getMessage());
        }
        return new LogoutResponse("Logged out successfully, refresh token invalidated ");

    }
}
