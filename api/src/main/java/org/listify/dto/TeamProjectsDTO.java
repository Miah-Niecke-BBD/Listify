package org.listify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamProjectsDTO {

    private Long projectID;
    private String projectName;

    public TeamProjectsDTO(Long projectID, String projectName) {
        this.projectID = projectID;
        this.projectName = projectName;
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
}
