public class UserDTO {
    private Long userID;
    private String username;
    private String githubID;

    public UserDTO(String username , Long userID, String githubID) {
        this.username = username;
        this.userID = userID;
        this.githubID = githubID;
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

    public String getGithubID() {
        return githubID;
    }
    public void setGithubID(String githubID) {
        this.githubID = githubID;
    }
}
