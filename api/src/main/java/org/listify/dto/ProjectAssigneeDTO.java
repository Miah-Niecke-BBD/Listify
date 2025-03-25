package org.listify.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)


public class ProjectAssigneeDTO {

    private String githubID;

    public ProjectAssigneeDTO(String githubID) {
        this.githubID = githubID;
    }

    public String getGithubID() {
        return githubID;
    }

    public void setGithubID(String githubID) {
        this.githubID = githubID;
    }
}