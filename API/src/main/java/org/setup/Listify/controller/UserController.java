package org.setup.Listify.controller;

import org.setup.Listify.exception.ErrorResponse;
import org.setup.Listify.exception.SuccessResponse;
import org.setup.Listify.exception.UserNotFoundException;
import org.setup.Listify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(@Param("userID") Long userID) {
        if (userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            userService.deleteUserByUserID(userID);
            SuccessResponse successResponse = new SuccessResponse("User successfully deleted.", HttpStatus.NO_CONTENT.value());
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (UserNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse("An error occurred while trying to delete the user.", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
