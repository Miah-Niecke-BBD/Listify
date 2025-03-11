package org.listify.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)


public class ProjectAssigneeDTO {


    private String projectName;
    private String githubID;

    public ProjectAssigneeDTO(String projectName, String githubID) {
        this.projectName = projectName;
        this.githubID = githubID;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGithubID() {
        return githubID;
    }

    public void setGithubID(String githubID) {
        this.githubID = githubID;
    }
}
