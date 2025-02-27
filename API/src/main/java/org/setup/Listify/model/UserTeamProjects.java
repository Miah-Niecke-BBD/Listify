package org.setup.Listify.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vUserTeamProjects", schema = "listify")
public class UserTeamProjects {

    @Id
    private Long userID;
    @Column(name = "githubID", nullable = false, length = 1700)
    private String githubID;
    @Column(name = "projectID", nullable = false)
    private Long projectID;
    @Column(name="projectName", nullable = false)
    private String projectName;
    @Column(name = "projectDescription", nullable = false, length = 500)
    private String projectDescription;
    @Column(name = "projectCreatedAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "projectUpdatedAt", nullable = true)
    private LocalDateTime updatedAt;
    @Column(name = "teamID", nullable = false)
    private Long teamID;
    @Column(name = "teamName", nullable = false, length = 100)
    private String teamName;
    @Column(name="teamCreatedAt", nullable = false)
    private LocalDateTime teamCreatedAt;
    @Column(name="teamUpdatedAt", nullable = false)
    private LocalDateTime teamUpdatedAt;


    protected UserTeamProjects() {}

    public UserTeamProjects(Long userID, String githubID, Long projectID, String projectName, String projectDescription, LocalDateTime createdAt, LocalDateTime updatedAt, Long teamID, String teamName, LocalDateTime teamCreatedAt, LocalDateTime teamUpdatedAt) {
        this.userID = userID;
        this.githubID = githubID;
        this.projectID = projectID;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.teamID = teamID;
        this.teamName = teamName;
        this.teamCreatedAt = teamCreatedAt;
        this.teamUpdatedAt = teamUpdatedAt;
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

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getGithubID() {
        return githubID;
    }

    public void setGithubID(String githubID) {
        this.githubID = githubID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDateTime getTeamCreatedAt() {
        return teamCreatedAt;
    }

    public void setTeamCreatedAt(LocalDateTime teamCreatedAt) {
        this.teamCreatedAt = teamCreatedAt;
    }

    public LocalDateTime getTeamUpdatedAt() {
        return teamUpdatedAt;
    }

    public void setTeamUpdatedAt(LocalDateTime teamUpdatedAt) {
        this.teamUpdatedAt = teamUpdatedAt;
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
