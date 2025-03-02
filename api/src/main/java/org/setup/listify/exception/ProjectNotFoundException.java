package org.setup.listify.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(Long id) {
        super("Could not find task with id: " + id);
    }
}
