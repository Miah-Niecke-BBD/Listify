package org.setup.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.setup.listify.handler.GitHubJwtService;
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
public class GitHubOAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private GitHubJwtService jwtService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/home")
    public ResponseEntity<?> showHomePage(@AuthenticationPrincipal OAuth2User principal,
                                          OAuth2AuthenticationToken authenticationToken,
                                          HttpServletRequest request) {
        try {
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

                // Check if the user exists, if not create a new one
                if (!userService.userExistsByGitHubID(gitHubID)) {
                    userService.createUser(gitHubID);
                }

                String accessToken;

                if(extractGitHubToken(authenticationToken) != null) {accessToken = extractGitHubToken(authenticationToken);}
                else {
                    accessToken = ExtractGitHubTokenFromSession(request);
                }

                request.getSession().setAttribute("githubAccessToken", accessToken);

                // Generate the JWT token
                String gitHubToken = jwtService.generateJwt();

                // Get the user from the database
                Users user = userService.getUserByGitHubID(gitHubID);

                // Return the token in the response
                return ResponseEntity.ok()
                        .body("{\"token\":\"" + gitHubToken + "\"}");
            }

            // Return Unauthorized if no principal is available
            return ResponseEntity.status(401).body("Unauthorized");
        } catch (IllegalArgumentException e) {
            // Handle known exceptions with a clear message
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        } catch (Exception e) {
            // Log the stack trace for debugging and return a generic error message
            e.printStackTrace();  // Log the error for debugging purposes

            // You can also log this exception to a logging framework like SLF4J or Log4j if you prefer.
            return ResponseEntity.status(500).body("{\"message\":\"An unexpected error occurred: " + e.getMessage() + "\", \"status\":500}");
        }
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
