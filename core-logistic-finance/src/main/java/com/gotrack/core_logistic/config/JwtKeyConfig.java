package com.gotrack.core_logistic.config;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.gateway")
public class JwtKeyConfig {

    private Resource publicKeyPath;

    private PublicKey gatewayPublicKey;

    public Resource getPublicKeyPath() {
        return publicKeyPath;
    }

    public void setPublicKeyPath(Resource publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    public PublicKey getGatewayPublicKey() throws Exception {
        if (gatewayPublicKey == null && publicKeyPath != null) {
            String publicKey = publicKeyPath.getContentAsString(StandardCharsets.UTF_8);
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
