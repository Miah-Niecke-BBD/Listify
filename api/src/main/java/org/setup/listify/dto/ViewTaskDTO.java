package org.setup.listify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewTaskDTO {

    private Long taskID;
    private String taskName;
    private String taskDescription;
    private String taskPriority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;
    private List<SimpleUserDTO> taskAssignees;
    private SimpleTaskDTO dependantTask;


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ViewTaskDTO(Long taskID, String taskName, String taskDescription,
                       String taskPriority, LocalDateTime createdAt, LocalDateTime updatedAt,
                       LocalDateTime dueDate, List<SimpleUserDTO> taskAssignees, SimpleTaskDTO dependantTask) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dueDate = dueDate;
        this.taskAssignees = taskAssignees;
        this.dependantTask = dependantTask;
    }


    public Long getTaskID() {
        return taskID;
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
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

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public List<SimpleUserDTO> getTaskAssignees() {
        return taskAssignees;
    }

    public void setTaskAssignees(List<SimpleUserDTO> taskAssignees) {
        this.taskAssignees = taskAssignees;
    }

    public SimpleTaskDTO getDependantTask() {
        return dependantTask;
    }

    public void setDependantTask(SimpleTaskDTO dependantTask) {
        this.dependantTask = dependantTask;
    }
}
