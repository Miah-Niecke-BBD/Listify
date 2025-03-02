package org.setup.Listify.controller;

import org.setup.Listify.exception.ErrorResponse;
import org.setup.Listify.model.TaskDependencies;
import org.setup.Listify.service.TaskDependenciesService;
import org.setup.Listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks/dependencies")
public class TaskDependenciesController {

    private final TaskDependenciesService taskDependenciesService;
    private final UserService userService;

    public TaskDependenciesController(TaskDependenciesService taskDependenciesService, UserService userService) {
        this.taskDependenciesService = taskDependenciesService;
        this.userService = userService;
    }

    
    @PostMapping
    @Transactional
    public ResponseEntity<?> newTaskDependency(Authentication authentication,
                                            @RequestParam(name = "taskID", required = false) Integer taskID,
                                            @RequestParam(name = "dependentTaskID", required = false) Integer dependentTaskID) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        if (taskID == null || dependentTaskID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newTaskDependencyID = taskDependenciesService.newTaskDependency(teamLeaderID, taskID, dependentTaskID);
        TaskDependencies newTaskDependency = taskDependenciesService.getTaskDependencyById(newTaskDependencyID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskDependency);
    }


    @DeleteMapping("/{dependentTaskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskDependencyByDependencyId(@PathVariable("dependentTaskID") Long dependentTaskID,
                                                                @RequestParam(name = "taskID", required = false) Integer taskID,
                                                                Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        if (taskID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Task ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        taskDependenciesService.deleteTaskDependencyByDependencyId(taskID, dependentTaskID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}
