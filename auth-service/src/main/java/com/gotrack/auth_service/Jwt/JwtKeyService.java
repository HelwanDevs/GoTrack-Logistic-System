package com.gotrack.auth_service.Jwt;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import lombok.Setter;

@ConfigurationProperties(prefix = "jwt")
@Component
@Setter
public class JwtKeyService {

    private Resource privateKeyPath;
    private Resource publicKeyPath;
    private Resource gatewayPublicKeyPath;
    private String secret;
    private boolean useHmac;

    public boolean isUseHmac() {
        return Boolean.TRUE.equals(useHmac);
    }

    public boolean isRsaMode() {
        return !Boolean.TRUE.equals(useHmac)
                && privateKeyPath != null
                && publicKeyPath != null;
    }

    public byte[] decodePem(String pem) {
        String base64 = pem.replaceAll(
                "-----BEGIN (RSA )?PRIVATE KEY-----|-----END (RSA )?PRIVATE KEY-----|-----BEGIN RSA PRIVATE KEY-----",
                "")
                .replaceAll(
                        "-----BEGIN (RSA )?PUBLIC KEY-----|-----END (RSA )?PUBLIC KEY-----|-----BEGIN PUBLIC KEY-----",
                        "")
                .trim()
                .replaceAll("\\s", "");
        return Base64.getDecoder().decode(base64);
    }

    public String loadPublicKeyBase64() {
        if (!isRsaMode())
            return null;
        try {
            String publicKey = publicKeyPath.getContentAsString(StandardCharsets.UTF_8);
            byte[] keyBytes = decodePem(publicKey);
            return Base64.getEncoder().encodeToString(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read public key from resource", e);
        }
    }

    public PrivateKey loadPrivateKey() {
        if (!isRsaMode())
            return null;
        try {
            String privateKey = privateKeyPath.getContentAsString(StandardCharsets.UTF_8);
            byte[] keyBytes = decodePem(privateKey);
            return java.security.KeyFactory.getInstance("RSA")
                    .generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse RSA private key", e);
        }
    }

    public PublicKey loadPublicKey() {
        if (!isRsaMode())
            return null;
        try {
            String publicKey = publicKeyPath.getContentAsString(StandardCharsets.UTF_8);
            byte[] keyBytes = decodePem(publicKey);
            return java.security.KeyFactory.getInstance("RSA")
                    .generatePublic(new java.security.spec.X509EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse RSA public key", e);
        }
    }

    public SecretKey loadSecretKey() {
        if (!Boolean.TRUE.equals(useHmac))
            return null;
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public PublicKey loadGatewayPublicKey() {
        try {
            String gatewayPublicKey = gatewayPublicKeyPath.getContentAsString(StandardCharsets.UTF_8);
            byte[] keyBytes = decodePem(gatewayPublicKey);
            return java.security.KeyFactory.getInstance("RSA")
                    .generatePublic(new java.security.spec.X509EncodedKeySpec(keyBytes));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse gateway RSA public key", e);
        }
    }
}
