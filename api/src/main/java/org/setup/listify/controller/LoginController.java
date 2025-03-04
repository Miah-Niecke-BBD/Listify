package org.setup.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.setup.listify.model.Users;
import org.setup.listify.service.UserService;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/home")
    public ResponseEntity<String> showHomePage(@AuthenticationPrincipal OAuth2User principal,
                                          OAuth2AuthenticationToken authenticationToken,
                                          HttpServletRequest request) {
        if (principal != null) {
            String gitHubID = extractGitHubID(principal);
            String gitHubToken = getGitHubToken(authenticationToken, request);

            if (!userService.userExistsByGitHubID(gitHubID)) {
                userService.createUser(gitHubID);
            }

            storeGitHubTokenInSession(request, gitHubToken);

            return ResponseEntity.ok().body("{\"token\":\"" + gitHubToken + "\"}");
        }

        return ResponseEntity.status(401).body("Unauthorized");
    }

    private String extractGitHubID(OAuth2User principal) {
        Object idAttribute = principal.getAttribute("id");

        if (idAttribute instanceof Integer) {
            return idAttribute.toString();
        } else if (idAttribute instanceof String) {
            return (String) idAttribute;
        }

        throw new IllegalArgumentException("Unexpected id type: " + idAttribute.getClass().getName());
    }

    private String getGitHubToken(OAuth2AuthenticationToken authenticationToken, HttpServletRequest request) {
        String token = extractGitHubToken(authenticationToken);
        if (token == null) {
            token = extractGitHubTokenFromSession(request);
        }
        return token;
    }

    private String extractGitHubToken(OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName());

        return (client != null && client.getAccessToken() != null) ? client.getAccessToken().getTokenValue() : null;
    }

    private String extractGitHubTokenFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (String) session.getAttribute("githubToken") : null;
    }

    private void storeGitHubTokenInSession(HttpServletRequest request, String gitHubToken) {
        HttpSession session = request.getSession(true);
        session.setAttribute("githubToken", gitHubToken);
    }
}
