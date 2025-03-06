package org.listify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@Table(name = "TaskAssignees", schema = "listify")
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public Long getTaskAssigneeID() {
        return taskAssigneeID;
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
