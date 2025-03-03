package org.setup.listify.controller;

import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.TaskDependencies;
import org.setup.listify.service.TaskDependenciesService;
import org.setup.listify.service.UserService;
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
                                            @RequestParam(name = "taskID") Integer taskID,
                                            @RequestParam(name = "dependentTaskID") Integer dependentTaskID) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        Long newTaskDependencyID = taskDependenciesService.newTaskDependency(teamLeaderID, taskID, dependentTaskID);
        TaskDependencies newTaskDependency = taskDependenciesService.getTaskDependencyById(newTaskDependencyID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskDependency);
    }


    @DeleteMapping("/{dependentTaskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskDependencyByDependencyId(@PathVariable("dependentTaskID") Long dependentTaskID,
                                                                @RequestParam(name = "taskID") Integer taskID,
                                                                Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        taskDependenciesService.deleteTaskDependencyByDependencyId(taskID, dependentTaskID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}
