package org.listify.dto;

public class SimpleUserDTO {
    private Long userID;

    private String githubID;

    public SimpleUserDTO(Long userID, String githubID) {
        this.userID = userID;
        this.githubID = githubID;
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
}
