package org.setup.listify.service;

import org.setup.listify.model.TaskAssignees;
import org.setup.listify.dto.UserAssignedTasksDTO;
import org.setup.listify.repo.TaskAssigneesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssigneesService {

    private final TaskAssigneesRepository repository;

    public TaskAssigneesService(TaskAssigneesRepository repository) {
        this.repository = repository;
    }

    public List<TaskAssignees> getAllAssignedTasks() {
        return repository.findAll();
    }

    public TaskAssignees getAssignedTaskById(Long taskAssigneeID) {
        return repository.findById(taskAssigneeID)
                .orElseThrow();
    }

    public List<UserAssignedTasksDTO> getTasksAssignedToSpecificUser(Long userID) {
        return repository.findAssignedTasksByUserID(userID);
    }

    public Long assignTaskToUser(int userID, int taskID) {
        repository.assignTaskToUser(userID, taskID);

        TaskAssignees newlyAssignedTask = repository.findTopOrderByTaskIDDesc();
        return newlyAssignedTask != null ? newlyAssignedTask.getTaskAssigneeID() : null;
    }

    public void deleteUserFromTask(int userID, int taskID, int teamLeaderID) {
        repository.deleteUserFromTask(userID, taskID, teamLeaderID);
    }
}
