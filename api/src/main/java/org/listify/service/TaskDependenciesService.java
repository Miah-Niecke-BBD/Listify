package org.listify.service;

import org.listify.exception.BadRequestException;
import org.listify.exception.NotFoundException;
import org.listify.model.TaskDependencies;
import org.listify.repo.TaskDependenciesRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskDependenciesService {

    private final TaskDependenciesRepository repository;
    private final TasksService tasksService;

    public TaskDependenciesService(TaskDependenciesRepository repository, TasksService tasksService) {
        this.repository = repository;
        this.tasksService = tasksService;
    }

    public TaskDependencies getTaskDependencyById(Long taskDependencyID) {
        return repository.findById(taskDependencyID)
                .orElseThrow(() -> new NotFoundException("No Task Dependency with ID: "+ taskDependencyID));
    }

    public Long newTaskDependency(Long teamLeaderID, Long taskID, Long dependentTaskID) {
        tasksService.validateUserAccessToTask(teamLeaderID, taskID);
        tasksService.validateUserAccessToTask(teamLeaderID, dependentTaskID);
        repository.newTaskDependency(teamLeaderID, taskID, dependentTaskID);

        TaskDependencies latestTaskDependency = repository.findTopOrderByTaskIDDesc();
        return latestTaskDependency != null ? latestTaskDependency.getTaskDependencyID() : null;
    }

    public void deleteTaskDependencyByDependencyId(Long taskID, Long taskDependencyID, Long teamLeaderID) {
        tasksService.validateUserAccessToTask(teamLeaderID, taskID);
        tasksService.validateUserAccessToTask(teamLeaderID, taskDependencyID);
        if (!repository.isTaskDependent(taskID, taskDependencyID)) {
            throw new BadRequestException("Task: "+taskID+" is not dependent on task: "+taskDependencyID);
        }
        repository.deleteTaskDependency(taskID, taskDependencyID, teamLeaderID);
    }
}
