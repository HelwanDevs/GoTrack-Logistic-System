package com.gotrack.api_gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
@ConfigurationProperties(prefix = "jwt.gateway")
public class JwtKeyConfig {

    private String privateKey;
    private String publicKey;
    private String clientPublicKeyString;

    private PrivateKey gatewayPrivateKey;
    private PublicKey gatewayPublicKey;
    private PublicKey clientPublicKey;

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setClientPublicKey(String clientPublicKey) {
        this.clientPublicKeyString = clientPublicKey;
    }

    public PrivateKey getGatewayPrivateKey() throws Exception {
        if (gatewayPrivateKey == null && privateKey != null) {
            gatewayPrivateKey = loadPrivateKey(privateKey);
        }
        return gatewayPrivateKey;
    }

    public PublicKey getGatewayPublicKey() throws Exception {
        if (gatewayPublicKey == null && publicKey != null) {
            gatewayPublicKey = loadPublicKey(publicKey);
        }
        return gatewayPublicKey;
    }

    public PublicKey getClientPublicKey() throws Exception {
        if (clientPublicKey == null && clientPublicKeyString != null) {
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
