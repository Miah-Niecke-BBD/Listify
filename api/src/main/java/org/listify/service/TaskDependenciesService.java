package org.listify.service;

import org.listify.exception.NotFoundException;
import org.listify.model.TaskDependencies;
import org.listify.repo.TaskDependenciesRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskDependenciesService {

    private final TaskDependenciesRepository repository;

    public TaskDependenciesService(TaskDependenciesRepository repository) {
        this.repository = repository;
    }

    public TaskDependencies getTaskDependencyById(Long taskDependencyID) {
        return repository.findById(taskDependencyID)
                .orElseThrow(() -> new NotFoundException("No Task Dependency with ID: "+ taskDependencyID));
    }

    public Long newTaskDependency(Long teamLeaderID, Long taskID, Long dependentTaskID) {
        repository.newTaskDependency(teamLeaderID, taskID, dependentTaskID);

        TaskDependencies latestTaskDependency = repository.findTopOrderByTaskIDDesc();
        return latestTaskDependency != null ? latestTaskDependency.getTaskDependencyID() : null;
    }

    public void deleteTaskDependencyByDependencyId(Long taskID, Long taskDependencyID, Long teamLeaderID) {
        repository.deleteTaskDependency(taskID, taskDependencyID, teamLeaderID);
    }
}
