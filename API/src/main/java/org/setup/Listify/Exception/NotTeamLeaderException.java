package org.setup.Listify.Exception;

public class NotTeamLeaderException extends RuntimeException {
    public NotTeamLeaderException(String message) {
        super(message);
    }
}