package org.setup.Listify.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "Tasks", schema = "listify")
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
    private int taskPosition;
    @Column(name = "dateCompleted", nullable = true)
    private LocalDateTime dateCompleted;
    @Column(name = "dueDate", nullable = true)
    private LocalDateTime dueDate;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt", nullable = true)
    private LocalDateTime updatedAt;

    protected Tasks() {
    }

    public Tasks(Long taskID, Long sectionID, Long parentTaskID, String taskName, String taskDescription, Long taskPriority, int taskPosition, LocalDateTime dateCompleted, LocalDateTime dueDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    @Override
    public String toString() {
        return "Task{" +
                "taskID=" + taskID +
                ", sectionID=" + sectionID +
                ", parentTaskID=" + parentTaskID +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskPriority=" + taskPriority +
                ", taskPosition=" + taskPosition +
                ", dateCompleted=" + dateCompleted +
                ", dueDate=" + dueDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public LocalDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public int getTaskPosition() {
        return taskPosition;
    }

    public void setTaskPosition(int taskPosition) {
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

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }
}

