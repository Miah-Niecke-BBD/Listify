package org.setup.Listify.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

<<<<<<< HEAD

=======
>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
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

<<<<<<< HEAD
    public Projects() {}
=======
    protected Projects() {}
>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c

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

<<<<<<< HEAD
=======
    public Long getProjectID() {
        return projectID;
    }

>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

<<<<<<< HEAD
=======
    public Long getTeamID() {
        return teamID;
    }

>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

<<<<<<< HEAD
=======
    public String getProjectName() {
        return projectName;
    }

>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

<<<<<<< HEAD
=======
    public String getProjectDescription() {
        return projectDescription;
    }

>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

<<<<<<< HEAD
=======
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

<<<<<<< HEAD
=======
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

>>>>>>> 632ef0892c82a4376c1d1519df00589b2370102c
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
