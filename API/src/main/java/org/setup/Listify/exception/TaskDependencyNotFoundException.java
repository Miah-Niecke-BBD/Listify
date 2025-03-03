package org.setup.listify.exception;

public class TaskDependencyNotFoundException extends RuntimeException {

    public TaskDependencyNotFoundException(Long id) {
        super("Could not find task dependency with id: " + id);
    }
}
