package org.listify.dto;

public class UpdateSectionPositionDTO {
    private int sectionPosition;

    public UpdateSectionPositionDTO(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }
}
