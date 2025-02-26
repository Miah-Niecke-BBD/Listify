package org.setup.Listify.Controller;

import org.setup.Listify.Exception.ErrorResponse;
import org.setup.Listify.Model.TaskDependencies;
import org.setup.Listify.Assembler.TaskDependenciesModelAssembler;
import org.setup.Listify.Service.TaskDependenciesService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks/dependencies")
public class TaskDependenciesController {

    private final TaskDependenciesService taskDependenciesService;
    private final TaskDependenciesModelAssembler assembler;

    public TaskDependenciesController(TaskDependenciesService taskDependenciesService, TaskDependenciesModelAssembler assembler) {
        this.taskDependenciesService = taskDependenciesService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<TaskDependencies>> getAllTaskDependencies() {
        List<TaskDependencies> taskDependencies = taskDependenciesService.getAllTaskDependencies();
        return assembler.toCollectionModel(taskDependencies);
    }

    @GetMapping("/{id}")
    public EntityModel<TaskDependencies> getTaskDependenciesById(@PathVariable Long id) {
        TaskDependencies taskDependency = taskDependenciesService.getTaskDependencyById(id);
        return assembler.toModel(taskDependency);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newTaskDependency(@RequestParam(required = false) Integer teamLeaderID,
                                            @RequestParam(required = false) Integer taskID,
                                            @RequestParam(required = false) Integer dependentTaskID) {

        if (teamLeaderID == null || taskID == null || dependentTaskID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newTaskDependencyID = taskDependenciesService.newTaskDependency(teamLeaderID, taskID, dependentTaskID);
        TaskDependencies newTaskDependency = taskDependenciesService.getTaskDependencyById(newTaskDependencyID);
        EntityModel<TaskDependencies> entityModel = assembler.toModel(newTaskDependency);
        return ResponseEntity.created((entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()))
                .body(entityModel);
    }

    @DeleteMapping("/{dependentTaskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskDependencyByDependencyId(@PathVariable Long dependentTaskID,
                                                                @RequestParam(required = false) Integer taskID,
                                                                @RequestParam(required = false) Integer teamLeaderID) {
        if (teamLeaderID == null || taskID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Task ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        taskDependenciesService.deleteTaskDependencyByDependencyId(taskID, dependentTaskID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Task Dependency with id: "+ dependentTaskID +" has been successfully deleted"));
    }
}
