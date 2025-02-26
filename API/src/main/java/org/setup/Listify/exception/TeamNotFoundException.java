package org.setup.Listify.exception;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(Long teamID) {
        super("Team not found with ID: " + teamID);
    }
}
