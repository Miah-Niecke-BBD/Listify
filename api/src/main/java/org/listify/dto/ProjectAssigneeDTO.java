package org.listify.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)


public class ProjectAssigneeDTO {

    private Long projectID;
    private String projectName;
    private String githubID;

    public ProjectAssigneeDTO(Long projectID, String projectName, String githubID) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.githubID = githubID;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
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
