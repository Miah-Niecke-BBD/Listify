
package org.setup.Listify.controller;

import org.setup.Listify.assembler.UserAssembler;
import org.setup.Listify.model.Users;
import org.setup.Listify.exception.UserNotFoundException;
import org.setup.Listify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/home")
public class GitHubOAuthController {

    private final UserService userService;
    private final UserAssembler userAssembler;

    @Autowired
    public GitHubOAuthController(UserService userService, UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @GetMapping("/home")
    public EntityModel<Users> getAuthenticatedUser(@AuthenticationPrincipal OAuth2User principal) {
        try {
            if (principal == null) {
                throw new UserNotFoundException("User is not authenticated.");
            }

            String githubId = principal.getAttribute("id");

            if(!userService.userExistsByGitHubID(githubId))
            {

                Users user = userService.createUser(githubId);
                return userAssembler.toModel(user);
            }else
            {
                Users user = userService.getUserByGitHubID(githubId);
                return userAssembler.toModel(user);
            }


        } catch (UserNotFoundException ex) {
            throw new RuntimeException("Error: Unable to fetch user information - " + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error occurred: " + ex.getMessage());
        }
    }
}
