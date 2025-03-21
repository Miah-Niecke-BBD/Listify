package org.listify.dto;

public class UserDTO {
    private Long userID;
    private String username;

    public UserDTO(String username , Long userID) {
        this.username = username;
        this.userID = userID;
    }

    public UserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserID() {
        return userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
