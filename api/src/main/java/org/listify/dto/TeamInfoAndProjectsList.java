package org.listify.dto;

import org.listify.model.Projects;
import org.listify.model.Teams;

import java.util.List;

public class TeamInfoAndProjectsList {

    private Teams team;
    private List<Projects> projects;

    public TeamInfoAndProjectsList(Teams team, List<Projects> projects) {
        this.team = team;
        this.projects = projects;
    }

    public Teams getTeam() {
        return team;
    }

    public void setTeam(Teams team) {
        this.team = team;
    }

    public List<Projects> getProjects() {
        return projects;
    }

    public void setProjects(List<Projects> projects) {
        this.projects = projects;
    }
}
