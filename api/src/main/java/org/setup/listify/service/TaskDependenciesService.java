package org.setup.listify.service;

import org.setup.listify.model.TaskDependencies;
import org.setup.listify.repo.TaskDependenciesRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskDependenciesService {

    private final TaskDependenciesRepository repository;

    public TaskDependenciesService(TaskDependenciesRepository repository) {
        this.repository = repository;
    }

    public TaskDependencies getTaskDependencyById(Long taskDependencyID) {
        return repository.findById(taskDependencyID)
                .orElseThrow();
    }

    public Long newTaskDependency(int teamLeaderID, int taskID, int dependentTaskID) {
        repository.newTaskDependency(teamLeaderID, taskID, dependentTaskID);

        TaskDependencies latestTaskDependency = repository.findTopOrderByTaskIDDesc();
        return latestTaskDependency != null ? latestTaskDependency.getTaskDependencyID() : null;
    }

    public void deleteTaskDependencyByDependencyId(int taskID, Long taskDependencyID, int teamLeaderID) {
        repository.deleteTaskDependency(taskID, taskDependencyID.intValue(), teamLeaderID);
    }
}
