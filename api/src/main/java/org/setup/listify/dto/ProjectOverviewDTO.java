package org.setup.listify.dto;

import java.util.List;

public class ProjectOverviewDTO {

    private Long projectID;
    private String projectName;
    private List<ProjectSectionDTO> sections;


    public ProjectOverviewDTO(Long projectID, String projectName, List<ProjectSectionDTO> sections) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.sections = sections;
    }

        public ProjectOverviewDTO()
        {}


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

    public List<ProjectSectionDTO> getSections() {
        return sections;
    }

    public void setSections(List<ProjectSectionDTO> sections) {
        this.sections = sections;
    }
}
