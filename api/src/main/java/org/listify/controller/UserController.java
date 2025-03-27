package org.listify.controller;

import org.listify.dto.UserDTO;
import org.listify.model.Users;
import org.listify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

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
        UserDTO userDto = null;
        Users user = userService.getUserByGitHubID(googleID);

        if (userService.userExistsByGitHubID(googleID)) {
            userDto = new UserDTO();
            userDto.setUsername(name);
            userDto.setUserID(user.getUserID());
             userDto.setGithubID(googleID);
        }
        return ResponseEntity.ok(userDto);
    }

}
