package org.setup.listify.service;

import org.setup.listify.exception.ForbiddenException;
import org.setup.listify.exception.NotFoundException;
import org.setup.listify.model.TaskAssignees;
import org.setup.listify.dto.UserAssignedTasksDTO;
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

    public List<UserAssignedTasksDTO> getTasksAssignedToSpecificUser(Long userID, Long loggedInUserID) {
        if (!repository.findUsersInSameProject(userID, loggedInUserID)) {
            throw new ForbiddenException("Cannot excess tasks of users that are not in the project");
        }

        List<UserAssignedTasksDTO> tasksAssignedToUser = repository.findAssignedTasksByUserID(userID);
        if (tasksAssignedToUser.isEmpty()) {
            throw new NotFoundException("There no tasks assigned to user: "+userID);
        }
        return tasksAssignedToUser;
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
        repository.deleteUserFromTask(userID, taskID, teamLeaderID);
    }
}
