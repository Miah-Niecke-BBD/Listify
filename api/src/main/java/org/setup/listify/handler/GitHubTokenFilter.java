package org.setup.listify.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.setup.listify.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;


@Component
@WebFilter("/*")
public class GitHubTokenFilter implements Filter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getRequestURI().startsWith("/login") ||
                httpServletRequest.getRequestURI().startsWith("/home") ||  // Skip /home
                httpServletRequest.getRequestURI().startsWith("/oauth2/authorization/") ||
                httpServletRequest.getRequestURI().startsWith("/login/oauth2/code/github")) {
            chain.doFilter(request, response);
            return;
        }

        String gitHubToken = extractGitHubToken(httpServletRequest);
        if (gitHubToken == null) {
            sendUnauthorizedResponse(httpServletResponse, "Unauthorized.");
            return;
        }


        try {
            validateGitHubToken(gitHubToken);
            authenticateUserWithGitHubToken(gitHubToken, httpServletRequest);
            chain.doFilter(request, response);
        } catch (BadCredentialsException e) {
            sendUnauthorizedResponse(httpServletResponse, "Invalid GitHub OAuth token.");
        }
    }

    private String extractGitHubToken(HttpServletRequest request) {
        String tokenFromSession = extractGitHubTokenFromSession(request);
        if (tokenFromSession != null) {
            return tokenFromSession;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    private String extractGitHubTokenFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("githubToken");
        }
        return null;
    }

    private void validateGitHubToken(String token) {
        String url = "https://api.github.com/user";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new BadCredentialsException("GitHub token invalid or expired.");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            String gitHubID = jsonResponse.get("id").asText();

            if (!userService.userExistsByGitHubID(gitHubID)) {
                throw new BadCredentialsException("User not found.");
            }

        } catch (Exception e) {
            throw new BadCredentialsException("GitHub token invalid or expired.");
        }
    }

    private void authenticateUserWithGitHubToken(String token, HttpServletRequest request) {
        try {

            String githubUserInfo = getGitHubUserInfo(token);
            GitHubUserPrincipal principal = new GitHubUserPrincipal(githubUserInfo);

            OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(
                    principal,
                    null,
                    "github"
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {

            throw new BadCredentialsException("GitHub token is invalid or expired.");
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

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(message);
    }
}
