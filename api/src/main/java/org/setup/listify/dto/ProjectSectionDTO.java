package org.setup.listify.dto;

import java.util.List;

public class ProjectSectionDTO {

    private Long sectionID;
    private String sectionName;
    private Byte sectionPosition;
    private List<SectionTaskDTO> tasks;  // List of tasks inside the section

    // Constructor, Getters, and Setters
    public ProjectSectionDTO(Long sectionID, String sectionName, Byte sectionPosition, List<SectionTaskDTO> tasks) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionPosition = sectionPosition;
        this.tasks = tasks;
    }

    // Getters and Setters
    public Long getSectionID() {
        return sectionID;
    }

    public void setSectionID(Long sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Byte getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(Byte sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public List<SectionTaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<SectionTaskDTO> tasks) {
        this.tasks = tasks;
    }
}
