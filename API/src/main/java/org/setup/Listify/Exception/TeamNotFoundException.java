package org.setup.Listify.Exception;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(Long teamID) {
        super("Team not found with ID: " + teamID);
    }
}
