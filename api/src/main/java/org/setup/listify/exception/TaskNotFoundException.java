package org.setup.listify.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Could not find task with id: " + id);
    }
}
