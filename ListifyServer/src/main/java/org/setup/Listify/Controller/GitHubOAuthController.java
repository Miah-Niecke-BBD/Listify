package org.setup.Listify.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GitHubOAuthController {


    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public GitHubOAuthController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/")
    public String loginOrRedirect(@AuthenticationPrincipal OAuth2User principal) {

        // If the user is authenticated, redirect to /home
        if (principal != null) {
            System.out.println("Method login() called");
            System.out.println("Authenticated with GitHub ID: " + principal.getAttribute("login"));
            return "redirect:/home";
        }


        return "index";
    }


    @GetMapping("/home")
    public String showHomePage(Model model, @AuthenticationPrincipal OAuth2User principal, OAuth2AuthenticationToken authenticationToken) {

        if (principal != null) {


            model.addAttribute("user", principal);



            // Retrieve the OAuth2AuthorizedClient for the current user and client (GitHub)
            OAuth2AuthorizedClient authorizedClient = authorizedClientService
                    .loadAuthorizedClient(authenticationToken.getAuthorizedClientRegistrationId(), authenticationToken.getName());

            // Get the access token
            String accessToken = authorizedClient.getAccessToken().getTokenValue();

            // Add the access token to the model (use this in your view if needed)
            model.addAttribute("accessToken", accessToken);

            return "home"; // Renders `src/main/resources/templates/home.html`
        }

        System.out.println("User is not authenticated");
        return "redirect:/";
    }
}