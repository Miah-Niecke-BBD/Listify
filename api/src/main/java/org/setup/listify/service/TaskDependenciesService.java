package org.setup.listify.service;

import org.setup.listify.exception.ListNotFoundException;
import org.setup.listify.model.TaskDependencies;
import org.setup.listify.repo.TaskDependenciesRepository;
import org.setup.listify.exception.TaskDependencyNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDependenciesService {

    private final TaskDependenciesRepository repository;

    public TaskDependenciesService(TaskDependenciesRepository repository) {
        this.repository = repository;
    }

    public List<TaskDependencies> getAllTaskDependencies() {
        List<TaskDependencies> taskDependencies = repository.findAll();
        if (taskDependencies.isEmpty()) {
            throw new ListNotFoundException("Task Dependencies");
        }
        return taskDependencies;
    }

    public TaskDependencies getTaskDependencyById(Long taskDependencyID) {
        return repository.findById(taskDependencyID)
                .orElseThrow(() -> new TaskDependencyNotFoundException(taskDependencyID));
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
