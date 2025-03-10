package org.listify.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ProjectAssignees", schema = "listify")
public class ProjectAssignees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectAssigneeID;
    @Column(name="userID", nullable = false)
    private Long userID;
    @Column(name="projectID", nullable = false)
    private Long projectID;

    public ProjectAssignees() {}

    public ProjectAssignees(Long projectAssigneeID, Long userID, Long projectID) {
        this.projectAssigneeID = projectAssigneeID;
        this.userID = userID;
        this.projectID = projectID;
    }

    public Long getProjectAssigneeID() {
        return projectAssigneeID;
    }

    public void setProjectAssigneeID(Long projectAssigneeID) {
        this.projectAssigneeID = projectAssigneeID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }
}
