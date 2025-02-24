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
