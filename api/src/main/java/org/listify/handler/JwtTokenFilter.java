package org.listify.handler;

import java.io.IOException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

        String requestUri = httpRequest.getRequestURI();

        if (isExcludedPath(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        String token = extractJwtToken(httpRequest);
        if (token == null) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("Token not found");
            return;
        }

        try {

            Map<String, String> userClaims = validateJwtToken(token);
            String sub = userClaims.get("sub");
            String name = userClaims.get("name");
            String email = userClaims.get("email");

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));


            Authentication authentication = new UsernamePasswordAuthenticationToken(sub, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            httpRequest.setAttribute("sub", sub);
            httpRequest.setAttribute("name", name);
            httpRequest.setAttribute("email", email);

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

    private boolean isExcludedPath(String requestUri) {
        return requestUri.startsWith("/login") ||
                requestUri.startsWith("/swagger-ui/") ||
                requestUri.startsWith("/v3/api-docs") ||
                requestUri.startsWith("/oauth2/callback/google") ||
                requestUri.startsWith("/authentication") ||
                requestUri.equals("/swagger-ui/index.html");
    }

    private Map<String, String> validateJwtToken(String token) throws BadCredentialsException {
        try {
            JWT jwt = JWTParser.parse(token);

            if (jwt instanceof SignedJWT signedJWT) {

                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                if (!validateJwtSignature(signedJWT)) {
                    throw new BadCredentialsException("Invalid JWT signature.");
                }

                validateJwtClaims(claimsSet);
                String sub = claimsSet.getSubject();
                String name = claimsSet.getStringClaim("name");
                String email = claimsSet.getStringClaim("email");

                Map<String, String> userClaims = new HashMap<>();
                userClaims.put("sub", sub);
                userClaims.put("name", name);
                userClaims.put("email", email);
                return userClaims;

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
            boolean signatureValid = signedJWT.verify(verifier);
            return signatureValid;
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
        } else {
            System.out.println("Failed to fetch public keys, status code: " + response.getStatusCode());
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
            throw new BadCredentialsException("Unable to extract public key from google cert.", e);
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
