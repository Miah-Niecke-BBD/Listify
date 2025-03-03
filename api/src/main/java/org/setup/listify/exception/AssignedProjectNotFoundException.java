package org.setup.listify.exception;

public class AssignedProjectNotFoundException extends RuntimeException {

    public AssignedProjectNotFoundException(Long id) {
        super("Could not find assigned project with id: " + id);
    }
}
