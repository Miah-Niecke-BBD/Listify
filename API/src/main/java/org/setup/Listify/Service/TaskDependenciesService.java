package org.setup.Listify.Service;

import org.setup.Listify.Model.TaskDependencies;
import org.setup.Listify.Repo.TaskDependenciesRepository;
import org.setup.Listify.exceptions.TaskDependencyNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskDependenciesService {

    private final TaskDependenciesRepository repository;

    public TaskDependenciesService(TaskDependenciesRepository repository) {
        this.repository = repository;
    }

    public List<TaskDependencies> getAllTaskDependencies() {
        return repository.findAll();
    }

    public TaskDependencies getTaskDependencyById(Long taskDependencyID) {
        return repository.findById(taskDependencyID)
                .orElseThrow(() -> new TaskDependencyNotFoundException(taskDependencyID));
    }
}
