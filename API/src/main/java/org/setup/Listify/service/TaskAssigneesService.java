package org.setup.Listify.service;

import org.setup.Listify.exception.ListNotFoundException;
import org.setup.Listify.model.TaskAssignees;
import org.setup.Listify.repo.TaskAssigneesRepository;
import org.setup.Listify.exception.AssignedTaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssigneesService {

    private final TaskAssigneesRepository repository;

    public TaskAssigneesService(TaskAssigneesRepository repository) {
        this.repository = repository;
    }

    public List<TaskAssignees> getAllAssignedTasks() {
        List<TaskAssignees> taskAssigneesList = repository.findAll();
        if (taskAssigneesList.isEmpty()) {
            throw new ListNotFoundException("Task Assignees");
        }
        return taskAssigneesList;
    }

    public TaskAssignees getAssignedTaskById(Long taskAssigneeID) {
        return repository.findById(taskAssigneeID)
                .orElseThrow(() -> new AssignedTaskNotFoundException(taskAssigneeID));
    }

    public List<TaskAssignees> getTasksAssignedToSpecificUser(Long userID) {
        List<TaskAssignees> tasksAssignedToUser = repository.findAssignedTasksByUserID(userID);
        if (tasksAssignedToUser.isEmpty()) {
            throw new ListNotFoundException("tasks assigned to user");
        }
        return tasksAssignedToUser;
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
