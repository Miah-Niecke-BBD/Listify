package org.setup.Listify.Handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret-key}")
    private  String secretKey;
    private static String staticSecretKey;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    @PostConstruct
    public void init() {
        staticSecretKey = secretKey;
    }

    public static String generateToken(String username) {

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(staticSecretKey));
    }

    public static boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(staticSecretKey))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(staticSecretKey))
                .build()
                .verify(token);
        return decodedJWT.getSubject();
    }
}
