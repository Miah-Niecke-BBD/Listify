package org.setup.listify.handler;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.interfaces.RSAPrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class GitHubJwtService {


    @Value("${github.jwt.private-key}")
    private String privateKeyText;

    @Value("${github.jwt.client-id}")
    private String clientId;


    public RSAPrivateKey loadPrivateKey() throws Exception {
        String privateKeyPEM = privateKeyText.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "");

        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    public String generateJwt() throws Exception {

        RSAPrivateKey privateKey = loadPrivateKey();

        long currentTime = System.currentTimeMillis() / 1000;
        long iat = currentTime - 60;
        long exp = currentTime + 600;


        return Jwts.builder()
                .setIssuedAt(new Date(iat * 1000)) //
                .setExpiration(new Date(exp * 1000))
                .claim("iss", clientId)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }
}
