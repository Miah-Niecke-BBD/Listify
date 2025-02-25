package org.setup.Listify.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AssignedTaskNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(AssignedTaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String assignedTaskNotFoundHandler(AssignedTaskNotFoundException ex) {
        return ex.getMessage();
    }
}
