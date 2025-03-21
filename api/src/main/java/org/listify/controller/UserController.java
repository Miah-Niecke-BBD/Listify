package org.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.listify.dto.UserDTO;
import org.listify.model.Users;
import org.listify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> deleteUser(@RequestParam("userID") Long userID , HttpServletRequest request) {

        Long currentUserID = userService.getUserIDFromAuthentication(request);

            userService.deleteUserByUserID(userID ,currentUserID);
            return ResponseEntity.noContent().build();

    }


    @GetMapping("/create")
    public ResponseEntity<Users> createUser(HttpServletRequest request) {

        String googleID = (String) request.getAttribute("sub");

        if (!userService.userExistsByGitHubID(googleID)) {
            userService.createUser(googleID);
        }
        Users user = userService.getUserByGitHubID(googleID);
        return ResponseEntity.ok(user);

    }

    @GetMapping
    public ResponseEntity<UserDTO> getUser(HttpServletRequest request) {

        String googleID = (String) request.getAttribute("sub");
        String name = (String) request.getAttribute("name");
        UserDTO user = null;
        if (userService.userExistsByGitHubID(googleID)) {
            user = new UserDTO();
            user.setUsername(name);
        }
        return ResponseEntity.ok(user);

    }

}
