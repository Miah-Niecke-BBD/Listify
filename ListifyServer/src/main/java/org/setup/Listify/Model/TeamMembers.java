package org.setup.Listify.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "TeamMembers", schema = "listify")
public class TeamMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamMemberID;

    @Column(name = "userID")
    private Long userID;

    @Column(name = "teamID")
    private Long teamID;

    @Column(name = "isTeamLeader")
    private boolean isTeamLeader;

    protected TeamMembers() {
    }

    public TeamMembers(Long teamMemberID, Long userID, Long teamID, boolean isTeamLeader) {
        this.teamMemberID = teamMemberID;
        this.userID = userID;
        this.teamID = teamID;
        this.isTeamLeader = isTeamLeader;
    }

    @Override
    public String toString() {
        return "TeamMember{" +
                "teamMemberID=" + teamMemberID +
                ", userID=" + userID +
                ", teamID=" + teamID +
                ", isTeamLeader=" + isTeamLeader +
                '}';
    }

    public Long getTeamMemberID() {
        return teamMemberID;
    }

    public void setTeamMemberID(Long teamMemberID) {
        this.teamMemberID = teamMemberID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getTeamID() {
        return teamID;
    }

    public void setTeamID(Long teamID) {
        this.teamID = teamID;
    }

    public boolean isTeamLeader() {
        return isTeamLeader;
    }

    public void setTeamLeader(boolean teamLeader) {
        isTeamLeader = teamLeader;
    }
}
