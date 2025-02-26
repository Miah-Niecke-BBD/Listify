<<<<<<< Updated upstream
package org.setup.Listify.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
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

    public Projects() {}

    public Projects(Long projectID, Long teamID, String projectName, String projectDescription, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.projectID = projectID;
        this.teamID = teamID;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", teamID=" + teamID +
                ", projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
=======
package org.setup.Listify.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Projects")
public class Projects {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectID;

    @Column(name = "teamID", nullable = false)
    private Long teamID;

    @Column(name = "projectName", nullable = false, length = 100)
    private String projectName;

    @Column(name = "projectDescription", nullable = false, length = 500)
    private String projectDescription;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    public Projects() {}

    public Projects(Long projectID, Long teamID, String projectName, String projectDescription, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.projectID = projectID;
        this.teamID = teamID;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", teamID=" + teamID +
                ", projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Getters

    public Long getProjectID() {
        return projectID;
    }

    public Long getTeamID() {
        return teamID;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
>>>>>>> Stashed changes
