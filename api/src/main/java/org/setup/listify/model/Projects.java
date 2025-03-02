package org.setup.listify.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Projects", schema = "listify")
public class Projects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectID;
    @Column(name="teamID", nullable = false)
    private Long teamID;
    @Column(name="projectName", nullable = false, length = 100)
    private String projectName;
    @Column(name="projectDescription", nullable = false, length = 500)
    private String projectDescription;
    @Column(name="createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name="updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    protected Projects() {}

    public Projects(Long projectID, Long teamID, String projectName, String projectDescription, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.projectID = projectID;
        this.teamID = teamID;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public Long getTeamID() {
        return teamID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
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
