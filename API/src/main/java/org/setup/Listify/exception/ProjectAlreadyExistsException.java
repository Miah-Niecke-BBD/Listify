package org.setup.Listify.exception;

public class ProjectAlreadyExistsException extends RuntimeException {

    public ProjectAlreadyExistsException(String projectName) {
        super("Project already exists: " + projectName);
    }
}
