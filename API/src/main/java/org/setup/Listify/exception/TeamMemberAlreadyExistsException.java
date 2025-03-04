package org.setup.listify.exception;

public class TeamMemberAlreadyExistsException extends RuntimeException {
    public TeamMemberAlreadyExistsException(String message) {
        super(message);
    }
}