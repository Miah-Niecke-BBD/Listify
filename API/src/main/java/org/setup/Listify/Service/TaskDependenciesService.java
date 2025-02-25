package org.setup.Listify.Service;

import org.setup.Listify.Exception.ListNotFoundException;
import org.setup.Listify.Model.TaskDependencies;
import org.setup.Listify.Repo.TaskDependenciesRepository;
import org.setup.Listify.Exception.TaskDependencyNotFoundException;
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
