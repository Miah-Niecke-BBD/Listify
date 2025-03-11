package org.listify.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.time.OffsetDateTime;


@Entity
@Table(name = "Tasks", schema = "listify")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskID;
    @Column(name = "sectionID", nullable = false)
    private Long sectionID;
    @Column(name = "parentTaskID", nullable = true)
    private Long parentTaskID;
    @Column(name = "taskName", nullable = false, length = 100)
    private String taskName;
    @Column(name = "taskDescription", nullable = true, length = 500)
    private String taskDescription;
    @Column(name = "taskPriority", nullable = true)
    private Long taskPriority;
    @Column(name = "taskPosition", nullable = false)
    private Integer taskPosition;
    @Column(name = "dateCompleted", nullable = true)
    private OffsetDateTime dateCompleted;
    @Column(name = "dueDate", nullable = true)
    private OffsetDateTime dueDate;
    @Column(name = "createdAt", nullable = false)
    private OffsetDateTime createdAt;
    @Column(name = "updatedAt", nullable = true)
    private OffsetDateTime updatedAt;

    protected Tasks() {
    }

    public Tasks(Long taskID, Long sectionID, Long parentTaskID, String taskName, String taskDescription, Long taskPriority, Integer taskPosition, OffsetDateTime dateCompleted, OffsetDateTime dueDate, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.taskID = taskID;
        this.sectionID = sectionID;
        this.parentTaskID = parentTaskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
        this.taskPosition = taskPosition;
        this.dateCompleted = dateCompleted;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public OffsetDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(OffsetDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Integer getTaskPosition() {
        return taskPosition;
    }

    public void setTaskPosition(Integer  taskPosition) {
        this.taskPosition = taskPosition;
    }

    public Long getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Long taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
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

    public Long getSectionID() {
        return sectionID;
    }

    public void setSectionID(Long sectionID) {
        this.sectionID = sectionID;
    }

    public Long getTaskID() {
        return taskID;
    }

}

