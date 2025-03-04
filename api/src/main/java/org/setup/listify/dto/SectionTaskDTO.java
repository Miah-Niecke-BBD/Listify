package org.setup.listify.dto;

import java.time.LocalDateTime;

public class SectionTaskDTO {
    private Integer taskID;
    private Integer parentTaskID;
    private String taskName;
    private String taskDescription;
    private Integer taskPriority;
    private Integer taskPosition;
    private LocalDateTime dueDate;
    private Integer userID;

    public SectionTaskDTO(Integer taskID, Integer parentTaskID, String taskName, String taskDescription, Integer taskPriority, Integer taskPosition, LocalDateTime dueDate, Integer userID) {
        this.taskID = taskID;
        this.parentTaskID = parentTaskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
        this.taskPosition = taskPosition;
        this.dueDate = dueDate;
        this.userID = userID;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getParentTaskID() {
        return parentTaskID;
    }

    public void setParentTaskID(Integer parentTaskID) {
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

    public Integer getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Integer taskPriority) {
        this.taskPriority = taskPriority;
    }

    public Integer getTaskPosition() {
        return taskPosition;
    }

    public void setTaskPosition(Integer taskPosition) {
        this.taskPosition = taskPosition;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}
