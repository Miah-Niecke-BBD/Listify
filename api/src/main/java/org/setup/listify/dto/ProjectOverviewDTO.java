package org.setup.listify.dto;

import java.util.List;

public class ProjectOverviewDTO {
    private Integer projectID;
    private String projectName;
    private List<ProjectSectionDTO> sections;


    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<ProjectSectionDTO> getSections() {
        return sections;
    }

    public void setSections(List<ProjectSectionDTO> sections) {
        this.sections = sections;
    }
}
