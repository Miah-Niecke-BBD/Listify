package org.setup.listify.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@WebFilter("/*")
public class JwtTokenFilter implements Filter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${github.jwt.public-key}")
    private String publicKeyText;

    private RSAPublicKey publicKey;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.publicKey = loadPublicKey();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getRequestURI().startsWith("/login") ||
                httpServletRequest.getRequestURI().startsWith("/home") ||
                httpServletRequest.getRequestURI().startsWith("/oauth2/authorization/") ||
                httpServletRequest.getRequestURI().startsWith("/login/oauth2/code/github")) {
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String accessToken = (String) httpServletRequest.getSession().getAttribute("githubAccessToken");
                setAuth(accessToken);
                validateJwtToken(token);
                chain.doFilter(httpServletRequest, httpServletResponse);
                return;
            } catch (Exception e) {
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpServletResponse.getWriter().write("Invalid or expired JWT token");
                return;
            }
        }


        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (oauth2Token != null) {
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(oauth2Token);
            chain.doFilter(request, response);  // Continue with the request
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().write("Authentication required");
        }


    }

    private Claims validateJwtToken(String token) throws Exception {
        try {


            Claims claims = Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                throw new Exception("Token has expired");
            }

            return claims;

        } catch (Exception e) {
            throw new Exception("Invalid JWT token", e);
        }
    }



    private RSAPublicKey loadPublicKey() {
        try {

            String publicKeyString = publicKeyText;
            byte[] decodedKey = Base64.getDecoder().decode(publicKeyString.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "").replace("\n", ""));

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    private String getGitHubUserInfo(String token) {
        String url = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new BadCredentialsException("GitHub token is invalid or expired.");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("GitHub token is invalid or expired.");
        }
    }

    public void setAuth(String accessToken)
    {
        String githubUserInfo = getGitHubUserInfo(accessToken);
        GitHubUserPrincipal principal = new GitHubUserPrincipal(githubUserInfo);

        OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(
                principal,
                null,
                "github"
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


}
