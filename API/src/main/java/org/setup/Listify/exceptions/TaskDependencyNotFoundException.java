package org.setup.Listify.exceptions;

public class TaskDependencyNotFoundException extends RuntimeException {

    public TaskDependencyNotFoundException(Long id) {
        super("Could not find task dependency with id: " + id);
    }
}
