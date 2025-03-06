package org.listify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Sections", schema = "listify")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionID;
    @Column(name = "projectID", nullable = false)
    private Long projectID;
    @Column(name = "sectionName", nullable = false, length = 100)
    private String sectionName;
    @Column(name = "sectionPosition", nullable = false)
    private int sectionPosition;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt", nullable = true)
    private LocalDateTime updatesAt;

    protected Sections() {};

    public Sections(Long sectionID, Long projectID, String sectionName, int sectionPosition, LocalDateTime createdAt, LocalDateTime updatesAt) {
        this.sectionID = sectionID;
        this.projectID = projectID;
        this.sectionName = sectionName;
        this.sectionPosition = sectionPosition;
        this.createdAt = createdAt;
        this.updatesAt = updatesAt;
    }

    public Long getSectionID() {
        return sectionID;
    }

    public void setSectionID(Long sectionID) {
        this.sectionID = sectionID;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
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

    public LocalDateTime getUpdatesAt() {
        return updatesAt;
    }

    public void setUpdatesAt(LocalDateTime updatesAt) {
        this.updatesAt = updatesAt;
    }
}

