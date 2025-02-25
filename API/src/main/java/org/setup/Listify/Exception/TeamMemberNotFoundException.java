package org.setup.Listify.Exception;

public class TeamMemberNotFoundException extends RuntimeException {
    public TeamMemberNotFoundException(Long userID) {
        super("Team member not found with ID: " + userID);
    }
}
