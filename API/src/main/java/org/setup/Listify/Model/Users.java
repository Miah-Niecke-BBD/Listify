package org.setup.Listify.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;
    @Column(name = "githubID", nullable = false, unique = true, length = 1700)
    private String gitHubID;
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt", nullable = true)
    private LocalDateTime updatedAt;

    protected Users() {
    }

    public Users(int userID, String gitHubID, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userID = userID;
        this.gitHubID = gitHubID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userID=" + userID +
                ", gitHubID='" + gitHubID + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
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
