package org.listify.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "Teams", schema = "listify")
public class Teams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamID;
    @Column(name = "teamName", nullable = false, length = 100)
    private String teamName;
    @Column(name = "createdAt", nullable = false)
    private OffsetDateTime createdAt;
    @Column(name = "updatedAt", nullable = true)
    private OffsetDateTime updatedAt;

    protected Teams () {}

    public Teams(Long teamID, String teamName, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.teamID = teamID;
        this.teamName = teamName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getTeamID() {
        return teamID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
