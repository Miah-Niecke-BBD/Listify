package org.setup.Listify.exception;

public class DeleteTeamLeaderException extends RuntimeException {

    public DeleteTeamLeaderException() {
        super("Can delete a team member from team");
    }
}

