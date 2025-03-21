package org.listify.dto;

public class UpdateTaskPositionDTO {

    private Long taskPosition;
    private Long sectionID;

    public UpdateTaskPositionDTO(Long taskPosition, Long sectionID) {
        this.taskPosition = taskPosition;
        this.sectionID = sectionID;
    }

    public Long getTaskPosition() {
        return taskPosition;
    }

    public void setTaskPosition(Long taskPosition) {
        this.taskPosition = taskPosition;
    }

    public Long getSectionID() {
        return sectionID;
    }

    public void setSectionID(Long sectionID) {
        this.sectionID = sectionID;
    }
}
