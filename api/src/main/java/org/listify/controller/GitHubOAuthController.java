package org.listify.controller;

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
import org.listify.model.Users;
import org.listify.service.UserService;

@RestController
public class GitHubOAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/home")
    public ResponseEntity<?> showHomePage(@AuthenticationPrincipal OAuth2User principal,
                                          OAuth2AuthenticationToken authenticationToken,
                                          HttpServletRequest request) {
        if (principal != null) {
            Object idAttribute = principal.getAttribute("id");
            String gitHubID;

            if (idAttribute instanceof Integer) {
                Integer id = principal.getAttribute("id");
                gitHubID = id.toString();
            } else if (idAttribute instanceof String) {
                gitHubID = principal.getAttribute("id");
            } else {
                throw new IllegalArgumentException("Unexpected id type: " + idAttribute.getClass().getName());
            }

            if (!userService.userExistsByGitHubID(gitHubID)) {
                userService.createUser(gitHubID);
            }
            String gitHubToken;

            Users user = userService.getUserByGitHubID(gitHubID);
            if(extractGitHubToken(authenticationToken) != null) {gitHubToken = extractGitHubToken(authenticationToken);}
            else {
                gitHubToken = ExtractGitHubTokenFromSession(request);
            }



            HttpSession session = request.getSession(true);
            session.setAttribute("githubToken", gitHubToken);

            return ResponseEntity.ok()
                    .body("{\"token\":\"" + gitHubToken + "\"}");
        }

        return ResponseEntity.status(401).body("Unauthorized");
    }


    private String extractGitHubToken(OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName());

        if (client != null && client.getAccessToken() != null) {
            return client.getAccessToken().getTokenValue();
        }

        return null;
    }
    private String ExtractGitHubTokenFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("githubToken");
        }
        return null;
    }

}

