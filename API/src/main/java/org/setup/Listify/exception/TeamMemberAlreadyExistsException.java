package org.setup.Listify.exception;

public class TeamMemberAlreadyExistsException extends RuntimeException {
    public TeamMemberAlreadyExistsException(String message) {
        super(message);
    }
}