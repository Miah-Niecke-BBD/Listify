package org.setup.Listify.Exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(Long id) {
        super("Could not find task with id: " + id);
    }
}
