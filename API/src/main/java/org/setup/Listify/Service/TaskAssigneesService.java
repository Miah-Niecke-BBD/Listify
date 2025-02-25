package org.setup.Listify.Service;

import org.setup.Listify.Model.TaskAssignees;
import org.setup.Listify.Repo.TaskAssigneesRepository;
import org.setup.Listify.Exception.AssignedTaskNotFoundException;
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
                .orElseThrow(() -> new AssignedTaskNotFoundException(taskAssigneeID));
    }

    public List<TaskAssignees> getTasksAssignedToSpecificUser(Long userID) {
        return repository.findAssignedTasksByUserID(userID);
    }

    public void assignTaskToUser(int userID, int taskID) {
        repository.assignTaskToUser(userID, taskID);
    }

    public void deleteUserFromTask(int userID, int taskID, int teamLeaderID) {
        repository.deleteUserFromTask(userID, taskID, teamLeaderID);
    }
}
