package com.gotrack.api_gateway.config;

import org.springframework.core.io.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@ConfigurationProperties(prefix = "jwt.gateway")
public class JwtKeyConfig {

    private Resource privateKeyPath;
    private Resource publicKeyPath;
    private Resource clientPublicKeyPath;

    private PrivateKey gatewayPrivateKey;
    private PublicKey gatewayPublicKey;
    private PublicKey clientPublicKey;

    public void setPrivateKeyPath(Resource privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public void setPublicKeyPath(Resource publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    public void setClientPublicKeyPath(Resource clientPublicKeyPath) {
        this.clientPublicKeyPath = clientPublicKeyPath;
    }

    public PrivateKey getGatewayPrivateKey() throws Exception {
        if (gatewayPrivateKey == null && privateKeyPath != null) {
            String privateKey = privateKeyPath.getContentAsString(StandardCharsets.UTF_8);
            gatewayPrivateKey = loadPrivateKey(privateKey);
        }
        return gatewayPrivateKey;
    }

    public PublicKey getGatewayPublicKey() throws Exception {
        if (gatewayPublicKey == null && publicKeyPath != null) {
            String publicKey = publicKeyPath.getContentAsString(StandardCharsets.UTF_8);
            gatewayPublicKey = loadPublicKey(publicKey);
        }
        return gatewayPublicKey;
    }

    public PublicKey getClientPublicKey() throws Exception {
        if (clientPublicKey == null && clientPublicKeyPath != null) {
            String clientPublicKeyString = clientPublicKeyPath.getContentAsString(StandardCharsets.UTF_8);
            clientPublicKey = loadPublicKey(clientPublicKeyString);
        }
        return clientPublicKey;
    }

    private PrivateKey loadPrivateKey(String pem) throws Exception {
        String base64 = pem.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .trim()
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
    }

    private PublicKey loadPublicKey(String pem) throws Exception {
        String base64 = pem.replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .trim()
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(base64);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(new X509EncodedKeySpec(keyBytes));
    }
}
