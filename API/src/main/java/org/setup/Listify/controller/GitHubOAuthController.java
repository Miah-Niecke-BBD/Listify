package org.setup.Listify.controller;

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
import org.setup.Listify.model.Users;
import org.setup.Listify.service.UserService;

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

            Integer id = principal.getAttribute("id");
            String gitHubID = id.toString();

            if (!userService.userExistsByGitHubID(gitHubID)) {
                userService.createUser(gitHubID);
            }

            Users user = userService.getUserByGitHubID(gitHubID);

            String gitHubToken = extractGitHubToken(authenticationToken);

            HttpSession session = request.getSession();
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
}
