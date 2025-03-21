package org.listify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionTaskDTO {

    private Long taskID;
    private String taskName;
    private Long parentTaskID;
    private Integer taskPosition;
    private OffsetDateTime createdAt;
    private OffsetDateTime dueDate;

    public SectionTaskDTO(Long taskID, String taskName, Long parentTaskID, Integer taskPosition, OffsetDateTime createdAt,
                          OffsetDateTime dueDate) {
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getTaskPosition() {
        return taskPosition;
    }

    public void setTaskPosition(Integer taskPosition) {
        this.taskPosition = taskPosition;
    }
}
