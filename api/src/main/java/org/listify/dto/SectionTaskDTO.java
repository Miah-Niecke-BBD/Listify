package org.listify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionTaskDTO {

    private Long taskID;
    private String taskName;
    private String taskPriority;
    private Long parentTaskID;
    private Integer taskPosition;
    private OffsetDateTime dateCompleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime dueDate;

    public SectionTaskDTO(Long taskID, String taskName,String taskPriority, Long parentTaskID, Integer taskPosition,OffsetDateTime dateCompleted, OffsetDateTime createdAt,
                          OffsetDateTime dueDate) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskPriority = taskPriority;
        this.parentTaskID = parentTaskID;
        this.taskPosition = taskPosition;
        this.dateCompleted = dateCompleted;
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

    public  String getTaskPriority() {return taskPriority;}

    public void setTaskPriority(String taskPriority) {

        this.taskPriority = taskPriority;
    }

    public Long getParentTaskID() {
        return parentTaskID;
    }

    public void setParentTaskID(Long parentTaskID) {
        this.parentTaskID = parentTaskID;
    }

    public OffsetDateTime getDueCompleted(){return dateCompleted;}

    public void setDateCompleted(OffsetDateTime dateCompleted){this.dateCompleted =dateCompleted;}


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
