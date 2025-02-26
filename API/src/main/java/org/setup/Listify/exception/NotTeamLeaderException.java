package org.setup.Listify.exception;

public class NotTeamLeaderException extends RuntimeException {
    public NotTeamLeaderException(String message) {
        super(message);
    }
}