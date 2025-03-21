package org.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.listify.model.TaskDependencies;
import org.listify.service.TaskDependenciesService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
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
    public ResponseEntity<TaskDependencies> createTaskDependency(HttpServletRequest request,
                                                                 @RequestParam(name = "taskID") Long taskID,
                                                                 @RequestParam(name = "dependentTaskID") Long dependentTaskID) {

        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        Long newTaskDependencyID = taskDependenciesService.newTaskDependency(teamLeaderID, taskID, dependentTaskID);
        TaskDependencies newTaskDependency = taskDependenciesService.getTaskDependencyById(newTaskDependencyID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskDependency);
    }


    @DeleteMapping("/{dependentTaskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskDependencyByDependencyId(@PathVariable("dependentTaskID") Long dependentTaskID,
                                                                @RequestParam(name = "taskID") Long taskID,
                                                                HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        taskDependenciesService.deleteTaskDependencyByDependencyId(taskID, dependentTaskID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }
}
