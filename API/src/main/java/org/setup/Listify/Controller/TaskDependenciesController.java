package org.setup.Listify.Controller;

import org.setup.Listify.Model.TaskDependencies;
import org.setup.Listify.Service.TaskDependenciesModelAssembler;
import org.setup.Listify.Service.TaskDependenciesService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskDependenciesController {

    private final TaskDependenciesService taskDependenciesService;
    private final TaskDependenciesModelAssembler assembler;

    public TaskDependenciesController(TaskDependenciesService taskDependenciesService, TaskDependenciesModelAssembler assembler) {
        this.taskDependenciesService = taskDependenciesService;
        this.assembler = assembler;
    }

    @GetMapping("/tasks/dependencies")
    public CollectionModel<EntityModel<TaskDependencies>> getAllTaskDependencies() {
        List<TaskDependencies> taskDependencies = taskDependenciesService.getAllTaskDependencies();
        return assembler.toCollectionModel(taskDependencies);
    }

    @GetMapping("tasks/dependencies/{id}")
    public EntityModel<TaskDependencies> getTaskDependenciesById(@PathVariable Long id) {
        TaskDependencies taskDependency = taskDependenciesService.getTaskDependencyById(id);
        return assembler.toModel(taskDependency);
    }
}
