package org.listify.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSectionsDTO {

    private Long sectionID;
    private String sectionName;
    private int sectionPosition;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSS")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSS")
    private  LocalDateTime updatedAt;

    public ProjectSectionsDTO(Long sectionID, String sectionName, int sectionPosition, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionPosition = sectionPosition;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
