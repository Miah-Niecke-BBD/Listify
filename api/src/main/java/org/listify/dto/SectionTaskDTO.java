package org.listify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionTaskDTO {

    private Long taskID;
    private String taskName;
    private Long parentTaskID;
    private Integer taskPosition;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

    public SectionTaskDTO(Long taskID, String taskName, Long parentTaskID, Integer taskPosition, LocalDateTime createdAt,
                          LocalDateTime dueDate) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.parentTaskID = parentTaskID;
        this.taskPosition = taskPosition;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
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

    public Long getParentTaskID() {
        return parentTaskID;
    }

    public void setParentTaskID(Long parentTaskID) {
        this.parentTaskID = parentTaskID;
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

    public Integer getTaskPosition() {
        return taskPosition;
    }

    public void setTaskPosition(Integer taskPosition) {
        this.taskPosition = taskPosition;
    }
}
