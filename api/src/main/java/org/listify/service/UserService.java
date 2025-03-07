package org.listify.service;

import jakarta.servlet.http.HttpServletRequest;
import org.listify.exception.BadRequestException;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.Authentication;
import org.listify.model.Users;
import org.listify.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
public class UserService {

    private final UsersRepository usersRepo;


    @Autowired
    public UserService(UsersRepository usersRepo) {
        this.usersRepo = usersRepo;
    }

    public Users getUserByGitHubID(String gitHubID) {

        if(userExistsByGitHubID(gitHubID)) {
            return usersRepo.findByGitHubID(gitHubID);
        }
        else {
            throw new NotFoundException("User with GitHub ID '%s' not found" + gitHubID);
        }

    }


    public void createUser(String gitHubID) {
        if (usersRepo.existsByGitHubID(gitHubID)) {
            throw new ForbiddenException("User with GitHub ID '%s' already exists" + gitHubID);
        }

        Users user = new Users();
        user.setGitHubID(gitHubID);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        usersRepo.createUser(user.getGitHubID());

    }

    public void deleteUserByUserID(Long userID , Long currentUserID) {

        if (userID > Long.MAX_VALUE / 2) {
            throw new BadRequestException("Number is too large.");
        }

        if (!usersRepo.existsByUserID(userID) || !currentUserID.equals(userID)) {
            throw new ForbiddenException("Not allowed to delete User "+ userID);
        }
        usersRepo.deleteUserByUserID(userID);
    }


    public boolean userExistsByGitHubID(String gitHubID) {
        return usersRepo.existsByGitHubID(gitHubID);
    }
    public boolean userExistsByUserID(Long userID) {
        return usersRepo.existsByUserID(userID);
    }


    public Long getUserIDFromAuthentication(HttpServletRequest request) {

        if(request!=null)
        {
            String googleID = (String) request.getAttribute("sub");

            if(googleID!=null)
            {
                Users user = usersRepo.findByGitHubID(googleID);

                if (userExistsByGitHubID(googleID)) {
                    return user.getUserID();
                } else {
                    throw new NotFoundException("User not found for GitHub ID: " + googleID);
                }
            }else
            {
                throw new NotFoundException("User not found for Google ID: " + googleID);
            }
        }
        else
        {
            throw new ForbiddenException("Request is null");
        }

    }

    public LocalDateTime getUserCreatedAt(Long userID) {
        Users user = usersRepo.findByUserID(userID);
        return user.getCreatedAt();
    }

    public Long getUserIDFromGithubID(String githubID) {
        if(userExistsByGitHubID(githubID)) {
            Users users = usersRepo.findByGitHubID(githubID);
            return users.getUserID();
        }
        else
        {
            throw new NotFoundException("User with github ID: " + githubID + " not found");
        }

    }

    public Users getUserByUserID(Long userID) {
        if(userExistsByUserID(userID)) {
            return usersRepo.findByUserID(userID);
        }else
        {
            throw new NotFoundException("User with user ID: " + userID + " not found");
        }
    }
}


