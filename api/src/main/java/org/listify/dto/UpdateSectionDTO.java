package org.listify.dto;

public class UpdateSectionDTO {
    private String sectionName;

    public UpdateSectionDTO(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
