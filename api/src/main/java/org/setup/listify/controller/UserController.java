package org.setup.listify.controller;

import org.setup.listify.model.Users;
import org.setup.listify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @DeleteMapping
    @Transactional
    public ResponseEntity<?> deleteUser(@RequestParam("userID") Long userID ,Authentication authentication) {

        Long currentUserID = userService.getUserIDFromAuthentication(authentication);


            userService.deleteUserByUserID(userID ,currentUserID);
            return ResponseEntity.noContent().build();

    }


    @PostMapping
    public ResponseEntity<Users> createUser(Authentication authentication) {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauthToken.getPrincipal();

        String googleID = oauth2User.getAttribute("sub");

        if (!userService.userExistsByGitHubID(googleID)) {
            userService.createUser(googleID);
        }
        Users user = userService.getUserByGitHubID(googleID);
        return ResponseEntity.ok(user);

    }

}
