package org.setup.Listify.service;

import org.setup.Listify.model.TaskAssignees;
import org.setup.Listify.repo.TaskAssigneesRepository;
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

    public List<TaskAssignees> getTasksAssignedToSpecificUser(Long userID) {
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
