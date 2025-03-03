package org.setup.listify.dto;

import java.time.LocalDateTime;

public class SectionTaskDTO {

    private Long taskID;
    private Long parentTaskID;
    private String taskName;
    private String taskDescription;
    private Long taskPriority;
    private Byte taskPosition;
    private LocalDateTime dueDate;
    private Long userID; // Assignee

    // Constructor, Getters, and Setters
    public SectionTaskDTO(Long taskID, Long parentTaskID, String taskName, String taskDescription, Long taskPriority,
                   Byte taskPosition, LocalDateTime dueDate, Long userID) {
        this.taskID = taskID;
        this.parentTaskID = parentTaskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
        this.taskPosition = taskPosition;
        this.dueDate = dueDate;
        this.userID = userID;
    }

    // Getters and Setters
    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public Long getParentTaskID() {
        return parentTaskID;
    }

    public void setParentTaskID(Long parentTaskID) {
        this.parentTaskID = parentTaskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Long getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Long taskPriority) {
        this.taskPriority = taskPriority;
    }

    public Byte getTaskPosition() {
        return taskPosition;
    }

    public void setTaskPosition(Byte taskPosition) {
        this.taskPosition = taskPosition;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
