package org.setup.Listify.Service;
import org.setup.Listify.Model.Users;
import org.setup.Listify.Exception.DuplicateUserException;
import org.setup.Listify.Exception.UserNotFoundException;
import org.setup.Listify.Repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UsersRepo usersRepo;


    @Autowired
    public UserService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public Users getUserByGitHubID(String gitHubID) {
        return usersRepo.findByGitHubID(gitHubID)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with GitHub ID '%s' not found", gitHubID)));
    }


    public Users createUser(String gitHubID) {
        if (usersRepo.existsByGitHubID(gitHubID)) {
            throw new DuplicateUserException(
                    String.format("User with GitHub ID '%s' already exists", gitHubID));
        }


        Users user = new Users();
        user.setGitHubID(gitHubID);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        usersRepo.createUser(user.getGitHubID());

        return user;
    }

    public void deleteUserByUserID(Long userID) {
        if (!usersRepo.existsByUserID(userID)) {
            throw new UserNotFoundException(
                    String.format("User with GitHub ID '%s' not found", userID));
        }
        usersRepo.deleteUserByUserID(userID);
    }


    public boolean userExistsByGitHubID(String gitHubID) {
        return usersRepo.existsByGitHubID(gitHubID);
    }
}