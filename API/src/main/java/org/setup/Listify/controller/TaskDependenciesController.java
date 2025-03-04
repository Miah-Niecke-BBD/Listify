package org.setup.listify.controller;

import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.TaskDependencies;
import org.setup.listify.assembler.TaskDependenciesModelAssembler;
import org.setup.listify.service.TaskDependenciesService;
import org.setup.listify.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks/dependencies")
public class TaskDependenciesController {

    private final TaskDependenciesService taskDependenciesService;
    private final UserService userService;
    private final TaskDependenciesModelAssembler assembler;

    public TaskDependenciesController(TaskDependenciesService taskDependenciesService, TaskDependenciesModelAssembler assembler, UserService userService) {
        this.taskDependenciesService = taskDependenciesService;
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<TaskDependencies>> getAllTaskDependencies() {
        List<TaskDependencies> taskDependencies = taskDependenciesService.getAllTaskDependencies();
        return assembler.toCollectionModel(taskDependencies);
    }

    @GetMapping("/{id}")
    public EntityModel<TaskDependencies> getTaskDependenciesById(@PathVariable("id") Long id) {
        TaskDependencies taskDependency = taskDependenciesService.getTaskDependencyById(id);
        return assembler.toModel(taskDependency);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newTaskDependency(Authentication authentication,
                                            @RequestParam(name = "taskID", required = false) Integer taskID,
                                            @RequestParam(name = "dependentTaskID", required = false) Integer dependentTaskID) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

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
    public ResponseEntity<?> deleteTaskDependencyByDependencyId(@PathVariable("dependentTaskID") Long dependentTaskID,
                                                                @RequestParam(name = "taskID", required = false) Integer taskID,
                                                                Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null || taskID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Task ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        taskDependenciesService.deleteTaskDependencyByDependencyId(taskID, dependentTaskID, teamLeaderID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Task Dependency with id: "+ dependentTaskID +" has been successfully deleted"));
    }
}
