package com.gotrack.user_branch_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@ConfigurationProperties(prefix = "jwt.gateway")
public class JwtKeyConfig {

    private String publicKey;

    private PublicKey gatewayPublicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public PublicKey getGatewayPublicKey() throws Exception {
        if (gatewayPublicKey == null && publicKey != null) {
            String base64 = publicKey.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .trim()
                    .replaceAll("\\s", "");
            byte[] keyBytes = Base64.getDecoder().decode(base64);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            gatewayPublicKey = kf.generatePublic(new X509EncodedKeySpec(keyBytes));
        }
        return gatewayPublicKey;
    }
}
