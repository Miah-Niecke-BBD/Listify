package org.setup.listify.service;

import org.setup.listify.exception.ForbiddenException;
import org.setup.listify.exception.NotFoundException;
import org.setup.listify.model.TaskAssignees;
import org.setup.listify.model.Users;
import org.setup.listify.repo.TaskAssigneesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssigneesService {

    private final TaskAssigneesRepository repository;
    private final UserService userService;

    public TaskAssigneesService(TaskAssigneesRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public TaskAssignees getAssignedTaskById(Long taskAssigneeID) {
        return repository.findById(taskAssigneeID)
                .orElseThrow(() -> new NotFoundException("Task assignment does not exist"));
    }


    public Long assignTaskToUser(String githubID, Long loggedInUserID, Long taskID) {
        Long userID = userService.getUserIDFromGithubID(githubID);

        if (!repository.findTaskAndUsersInProject(taskID, loggedInUserID, userID)) {
            throw new ForbiddenException("Only users in the project can be assigned to tasks");
        }

        repository.assignTaskToUser(userID, taskID);
        TaskAssignees newlyAssignedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAssignedTask != null ? newlyAssignedTask.getTaskAssigneeID() : null;
    }

    public void deleteUserFromTask(Long userID, Long taskID, Long teamLeaderID) {
        if (!repository.findUserAndTaskInProject(userID, taskID)) {
            throw new ForbiddenException("Tasks and Users should both exist in the project");
        }
        if (!isUserAssignedToTask(taskID, userID)) {
            throw new ForbiddenException("User "+userID+" is not assigned to task: "+taskID);
        }
        repository.deleteUserFromTask(userID, taskID, teamLeaderID);
    }

    public List<Users> getUsersAssignedToTask(Long taskID, Long loggedInUserID) {
        if (!repository.findUserAndTaskInProject(loggedInUserID, taskID)) {
            throw new ForbiddenException("Cannot excess tasks that are not in the project");
        }
        List<Users> usersAssignedToTask = repository.getUsersAssignedToTask(taskID);
        if (usersAssignedToTask.isEmpty()) {
            throw new NotFoundException("There no users assigned to task: "+taskID);
        }
        return usersAssignedToTask;
    }

    public boolean isUserAssignedToTask(Long taskID, Long userID) {
        return repository.isUserAssignedToTask(taskID, userID);
    }
}
