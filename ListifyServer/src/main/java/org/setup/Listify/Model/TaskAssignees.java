package org.setup.Listify.Model;

import jakarta.persistence.*;

@Entity
public class TaskAssignees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskAssigneeID;
    @Column(name = "userID", nullable = false)
    private Long userID;
    @Column(name = "taskID", nullable = false)
    private Long taskID;

    protected TaskAssignees() {
    }

    public TaskAssignees(Long taskAssigneeID, Long userID, Long taskID) {
        this.taskAssigneeID = taskAssigneeID;
        this.userID = userID;
        this.taskID = taskID;
    }

    @Override
    public String toString() {
        return "TaskAssignee{" +
                "taskAssigneeID=" + taskAssigneeID +
                ", userID=" + userID +
                ", taskID=" + taskID +
                '}';
    }

    public Long getTaskAssigneeID() {
        return taskAssigneeID;
    }

    public void setTaskAssigneeID(Long taskAssigneeID) {
        this.taskAssigneeID = taskAssigneeID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }
}
