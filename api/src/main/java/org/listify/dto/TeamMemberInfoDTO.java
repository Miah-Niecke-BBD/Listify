package org.listify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamMemberInfoDTO {

    private Long userID;
    private String githubID;
    private boolean isTeamLeader;

    public TeamMemberInfoDTO(Long userID, String githubID, boolean isTeamLeader) {
        this.userID = userID;
        this.githubID = githubID;
        this.isTeamLeader = isTeamLeader;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getGithubID() {
        return githubID;
    }

    public void setGithubID(String githubID) {
        this.githubID = githubID;
    }

    public boolean isTeamLeader() {
        return isTeamLeader;
    }

    public void setTeamLeader(boolean teamLeader) {
        isTeamLeader = teamLeader;
    }
}
