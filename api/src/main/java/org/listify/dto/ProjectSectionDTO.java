package org.listify.dto;

import java.util.List;

public class ProjectSectionDTO {
    private Integer sectionID;
    private String sectionName;
    private Integer sectionPosition;
    private List<SectionTaskDTO> tasks;

    public ProjectSectionDTO(Integer sectionID, String sectionName, Integer sectionPosition, List<SectionTaskDTO> tasks) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionPosition = sectionPosition;
        this.tasks = tasks;
    }

    // Getters and Setters
    public Integer getSectionID() {
        return sectionID;
    }

    public void setSectionID(Integer sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Integer getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(Integer sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public List<SectionTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<SectionTaskDTO> tasks) {
        this.tasks = tasks;
    }
}
