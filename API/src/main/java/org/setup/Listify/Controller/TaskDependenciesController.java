package org.setup.Listify.Controller;

import org.setup.Listify.Model.TaskDependencies;
import org.setup.Listify.Assembler.TaskDependenciesModelAssembler;
import org.setup.Listify.Service.TaskDependenciesService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/tasks/dependencies")
    @Transactional
    public ResponseEntity<?> newTaskDependency(@RequestParam int teamLeaderID,
                                            @RequestParam int taskID,
                                            @RequestParam int dependentTaskID) {
        taskDependenciesService.newTaskDependency(teamLeaderID, taskID, dependentTaskID);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Task dependency created successfully"));
    }

    @DeleteMapping("/tasks/dependencies/{dependentTaskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskDependencyByDependencyId(@PathVariable Long dependentTaskID,
                                                                @RequestParam int taskID,
                                                                @RequestParam int teamLeaderID) {

        taskDependenciesService.deleteTaskDependencyByDependencyId(taskID, dependentTaskID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Task Dependency with id: "+ dependentTaskID +" has been successfully deleted"));
    }
}
