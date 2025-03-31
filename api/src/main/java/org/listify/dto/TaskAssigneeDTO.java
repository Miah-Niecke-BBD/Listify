package org.listify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.listify.model.Users;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskAssigneeDTO {


    private Long userID;
    private String githubID;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TaskAssigneeDTO() {
    }

    public TaskAssigneeDTO(Long userID, String githubID, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userID = userID;
        this.githubID = githubID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getGitHubID() {
        return githubID;
    }

    public void setGitHubID(String githubID) {
        this.githubID = githubID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
