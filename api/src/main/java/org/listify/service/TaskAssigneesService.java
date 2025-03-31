package org.listify.service;

import org.listify.dto.TaskAssigneeDTO;
import org.listify.exception.ForbiddenException;
import org.listify.exception.NotFoundException;
import org.listify.model.TaskAssignees;
import org.listify.model.Users;
import org.listify.repo.TaskAssigneesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void deleteUserFromTask(String githubID, Long taskID, Long teamLeaderID) {
        Long userID = userService.getUserIDFromGithubID(githubID);

        if (!repository.findUserAndTaskInProject(userID, taskID)) {
            throw new ForbiddenException("Tasks and Users should both exist in the project");
        }
        if (!isUserAssignedToTask(taskID, userID)) {
            throw new ForbiddenException("User "+userID+" is not assigned to task: "+taskID);
        }
        repository.deleteUserFromTask(userID, taskID, teamLeaderID);
    }

    public List<TaskAssigneeDTO> getUsersAssignedToTask(Long taskID, Long loggedInUserID) {

        if (!repository.findUserAndTaskInProject(loggedInUserID, taskID)) {
            throw new ForbiddenException("Cannot access tasks that are not in the project");
        }


        List<Users> usersAssignedToTask = repository.getUsersAssignedToTask(taskID);


        if (usersAssignedToTask.isEmpty()) {
            throw new NotFoundException("There are no users assigned to task: " + taskID);
        }


        return usersAssignedToTask.stream()
                .map(this::convertUserToTaskAssigneeDTO)
                .collect(Collectors.toList());
    }

    private TaskAssigneeDTO convertUserToTaskAssigneeDTO(Users user) {
        return new TaskAssigneeDTO(
                user.getUserID(),
                user.getGitHubID(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public boolean isUserAssignedToTask(Long taskID, Long userID) {
        return repository.isUserAssignedToTask(taskID, userID);
    }
}
