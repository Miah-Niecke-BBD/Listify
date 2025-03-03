package org.setup.listify.service;

import org.setup.listify.exception.NotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.setup.listify.model.Users;
import org.setup.listify.exception.DuplicateUserException;
import org.setup.listify.exception.UserNotFoundException;
import org.setup.listify.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepo;


    @Autowired
    public UserService(UsersRepository usersRepo) {
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

    public Long getUserIDFromAuthentication(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauthToken.getPrincipal();

        String gitHubID = oauth2User.getAttribute("id").toString();

        Optional<Users> user = usersRepo.findByGitHubID(gitHubID);

        if (user.isPresent()) {
            return user.get().getUserID();
        } else {
            throw new RuntimeException("User not found for GitHub ID: " + gitHubID);
        }
    }

    public LocalDateTime getUserCreatedAt(Long userID) {
        Users user = usersRepo.findByUserID(userID).orElseThrow(() -> new NotFoundException("User not found"));
        return user.getCreatedAt();
    }
}

