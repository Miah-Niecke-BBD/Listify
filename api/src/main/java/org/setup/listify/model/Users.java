package org.setup.listify.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users", schema = "Listify")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @Column(name = "githubID", nullable = false, unique = true, length = 1700)
    private String gitHubID;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt", nullable = true)
    private LocalDateTime updatedAt;

    public Users() {
    }

    public Users(Long userID, String gitHubID, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userID = userID;
        this.gitHubID = gitHubID;
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
        return gitHubID;
    }

    public void setGitHubID(String gitHubID) {
        this.gitHubID = gitHubID;
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
