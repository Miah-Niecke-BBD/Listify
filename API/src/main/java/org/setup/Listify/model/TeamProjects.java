package org.setup.Listify.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="vTeamProjects", schema="listify")
public class TeamProjects {

    private Long teamID;
    @Column(name = "teamName", nullable = false, length = 100)
    private String teamName;
    @Id
    private Long projectID;
    @Column(name="projectDescription", nullable = false, length = 500)
    private String projectDescription;
    @Column(name = "projectCreatedAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "projectUpdatedAt", nullable = true)
    private LocalDateTime updatedAt;

    protected TeamProjects () {}

    public TeamProjects(Long teamID, String teamName, Long projectID, String projectDescription, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.projectID = projectID;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamID=" + teamID +
                ", teamName='" + teamName + '\'' +
                "projectID=" + projectID +
                "projectDescription=" + projectDescription +
                ", projectCreatedAt=" + createdAt +
                ", projectUpdatedAt=" + updatedAt +
                '}';
    }

    public Long getTeamID() {
        return teamID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
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
