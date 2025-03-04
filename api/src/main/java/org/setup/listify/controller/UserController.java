package org.setup.listify.controller;

import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.exception.NotFoundException;
import org.setup.listify.exception.UserNotFoundException;
import org.setup.listify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping
    @Transactional
    public ResponseEntity<Object> deleteUser(@RequestParam("userID") Long userID) {

            userService.deleteUserByUserID(userID);
            return ResponseEntity.ok().build();

    }
}
