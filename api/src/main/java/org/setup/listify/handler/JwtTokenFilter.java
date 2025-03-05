package org.setup.listify.handler;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class JwtTokenFilter implements Filter {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientID;
    @Value("${accounts.google.com}")
    private String googleAccountUrl;
    @Value("${google.jwt.public.keys.url}")
    private String GOOGLE_JWT_PUBLIC_KEYS_URL;

    private static final Map<String, PublicKey> publicKeyCache = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().startsWith("/swagger-ui/") ||
                httpRequest.getRequestURI().startsWith("/v3/api-docs") ||
                httpRequest.getRequestURI().startsWith("/getToken") ||
                httpRequest.getRequestURI().equals("/login") ||
                httpRequest.getRequestURI().startsWith("/login/oauth2/code/google")) {
            chain.doFilter(request, response);
            return;
        }

        String token = extractJwtToken(httpRequest);
        if (token == null) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("Token is not Found");
            return;
        }

        try {
            validateJwtToken(token);
            chain.doFilter(request, response);
        } catch (BadCredentialsException e) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("Invalid JWT token: " + e.getMessage());
        }
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void validateJwtToken(String token) throws BadCredentialsException {
        try {
            JWT jwt = JWTParser.parse(token);

            if (jwt instanceof SignedJWT signedJWT) {

                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                if (!validateJwtSignature(signedJWT)) {
                    throw new BadCredentialsException("Invalid JWT signature.");
                }

                validateJwtClaims(claimsSet);
            } else {
                throw new BadCredentialsException("Invalid JWT structure.");
            }
        } catch (ParseException e) {
            throw new BadCredentialsException("Error parsing JWT token: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new BadCredentialsException("JWT token validation failed: " + e.getMessage(), e);
        }
    }

    private boolean validateJwtSignature(SignedJWT signedJWT) throws Exception {
        String kid = signedJWT.getHeader().getKeyID();

        PublicKey publicKey = getPublicKeyFromCache(kid);
        if (publicKey == null) {
            publicKey = fetchAndCachePublicKey(kid);
        }

        if (publicKey != null) {
            RSASSAVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
            return signedJWT.verify(verifier);
        }

        return false;
    }

    private PublicKey fetchAndCachePublicKey(String kid) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_JWT_PUBLIC_KEYS_URL, HttpMethod.GET, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String publicKeysJson = response.getBody();
            PublicKey publicKey = extractPublicKeyFromGoogleCerts(publicKeysJson, kid);

            if (publicKey != null) {
                cachePublicKey(kid, publicKey);
            }
            return publicKey;
        }

        return null;
    }

    private void validateJwtClaims(JWTClaimsSet claimsSet) throws BadCredentialsException {
        long exp = claimsSet.getExpirationTime().getTime();
        if (System.currentTimeMillis() > exp) {
            throw new BadCredentialsException("JWT token has expired.");
        }

        String issuer = claimsSet.getIssuer();
        if (!googleAccountUrl.equals(issuer)) {
            throw new BadCredentialsException("Invalid JWT token issuer.");
        }

        List<String> audience = claimsSet.getAudience();
        if (audience == null || !audience.contains(googleClientID)) {
            throw new BadCredentialsException("Invalid JWT token audience.");
        }
    }

    private PublicKey extractPublicKeyFromGoogleCerts(String googleCertsJson, String kid) {
        try {
            JSONObject jsonResponse = new JSONObject(googleCertsJson);
            JSONArray keys = jsonResponse.getJSONArray("keys");

            for (int i = 0; i < keys.length(); i++) {
                JSONObject key = keys.getJSONObject(i);

                if (key.getString("kid").equals(kid)) {
                    JWK jwk = JWK.parse(key.toString());

                    if (jwk instanceof RSAKey rsaKey) {
                        return rsaKey.toRSAPublicKey();
                    }
                }
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Unable to extract public key from google cert.");
        }
        return null;
    }

    private PublicKey getPublicKeyFromCache(String kid) {
        return publicKeyCache.get(kid);
    }

    private void cachePublicKey(String kid, PublicKey publicKey) {
        publicKeyCache.put(kid, publicKey);
    }
}
