package org.listify.dto;

import java.time.LocalDateTime;


public class UserAssignedTasksDTO {

    private Long userID;
    private String gitHubID;
    private Long taskID;
    private String taskName;
    private String taskDescription;
    private Long taskPriority;
    private LocalDateTime dueDate;
    private LocalDateTime dateCompleted;
    private LocalDateTime taskCreatedAt;
    private LocalDateTime taskUpdatedAt;
    private Long sectionID;
    private String sectionName;
    private Long projectID;
    private String projectName;

    public UserAssignedTasksDTO() {}

    public UserAssignedTasksDTO(Long userID, String gitHubID, Long taskID, String taskName, String taskDescription,
                                Long taskPriority, LocalDateTime dueDate, LocalDateTime dateCompleted,
                                LocalDateTime taskCreatedAt, LocalDateTime taskUpdatedAt,
                                Long sectionID, String sectionName, Long projectID, String projectName) {
        this.userID = userID;
        this.gitHubID = gitHubID;
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskPriority = taskPriority;
        this.dueDate = dueDate;
        this.dateCompleted = dateCompleted;
        this.taskCreatedAt = taskCreatedAt;
        this.taskUpdatedAt = taskUpdatedAt;
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.projectID = projectID;
        this.projectName = projectName;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getGitHubID() {
        return gitHubID;
    }

    public void setGitHubID(String gitHubID) {
        this.gitHubID = gitHubID;
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

    public Long getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Long taskPriority) {
        this.taskPriority = taskPriority;
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

    public LocalDateTime getTaskCreatedAt() {
        return taskCreatedAt;
    }

    public void setTaskCreatedAt(LocalDateTime taskCreatedAt) {
        this.taskCreatedAt = taskCreatedAt;
    }

    public LocalDateTime getTaskUpdatedAt() {
        return taskUpdatedAt;
    }

    public void setTaskUpdatedAt(LocalDateTime taskUpdatedAt) {
        this.taskUpdatedAt = taskUpdatedAt;
    }

    public Long getSectionID() {
        return sectionID;
    }

    public void setSectionID(Long sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
