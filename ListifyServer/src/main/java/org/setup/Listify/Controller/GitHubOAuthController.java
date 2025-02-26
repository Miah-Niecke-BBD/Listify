package org.setup.Listify.Controller;

import org.setup.Listify.Model.Users;
import org.setup.Listify.Service.UserService;
import org.setup.Listify.Handler.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubOAuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public ResponseEntity<?> showHomePage(@AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            Integer id = principal.getAttribute("id");
            String gitHubID = id.toString();

            if (!userService.userExistsByGitHubID(gitHubID)) {
                userService.createUser(gitHubID);
            }

            Users user = userService.getUserByGitHubID(gitHubID);
            String jwtToken = JwtTokenUtil.generateToken(principal.getName());


            return ResponseEntity.ok()
                    .body("{\"token\":\"" + jwtToken + "\"}");
        }

        return ResponseEntity.status(401).body("Unauthorized");
    }
}
