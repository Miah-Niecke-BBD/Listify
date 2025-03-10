package org.listify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateTaskDTO {

    private String taskName;
    private String taskDescription;
    private Byte taskPriority;
    private OffsetDateTime dueDate;
    private OffsetDateTime dateCompleted;

    public UpdateTaskDTO(String taskName, String taskDescription, Byte taskPriority, OffsetDateTime dueDate, OffsetDateTime dateCompleted) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
        this.dueDate = dueDate;
        this.dateCompleted = dateCompleted;
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

    public Byte getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Byte taskPriority) {
        this.taskPriority = taskPriority;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public OffsetDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(OffsetDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
}
