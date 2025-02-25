package org.setup.Listify.Exception;

public class TeamMemberAlreadyExistsException extends RuntimeException {
    public TeamMemberAlreadyExistsException(String message) {
        super(message);
    }
}