package org.listify.dto;

public class UpdateTeamDTO {
    private String newTeamName;

    public UpdateTeamDTO() {
    }

    public UpdateTeamDTO(String newTeamName) {
        this.newTeamName = newTeamName;
    }

    public String getNewTeamName() {
        return newTeamName;
    }

    public void setNewTeamName(String newTeamName) {
        this.newTeamName = newTeamName;
    }
}
