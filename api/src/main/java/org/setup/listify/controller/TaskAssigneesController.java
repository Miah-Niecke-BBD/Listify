package org.setup.listify.controller;

import org.setup.listify.model.TaskAssignees;
import org.setup.listify.dto.UserAssignedTasksDTO;
import org.setup.listify.service.TaskAssigneesService;
import org.setup.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/assigned")
public class TaskAssigneesController {

    private final TaskAssigneesService taskAssigneesService;
    private final UserService userService;


    public TaskAssigneesController(TaskAssigneesService taskAssigneesService, UserService userService) {
        this.taskAssigneesService = taskAssigneesService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllAssignedTasks() {
        List<TaskAssignees> assignedTasks = taskAssigneesService.getAllAssignedTasks();
        if (assignedTasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no assigned tasks");
        }
        return ResponseEntity.ok(assignedTasks);
    }


    @GetMapping("/user/{userID}")
    public ResponseEntity<Object> getTasksAssignedToSpecificUser(
            @PathVariable("userID") Long userID) {

        List<UserAssignedTasksDTO> tasksAssignedToUser = taskAssigneesService
                .getTasksAssignedToSpecificUser(userID);

        if (tasksAssignedToUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There no tasks assigned to user: "+userID);
        }
        return ResponseEntity.ok(tasksAssignedToUser);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> assignTask(Authentication authentication,
                                        @RequestParam(name = "taskID") Long taskID) {
        Long userID = userService.getUserIDFromAuthentication(authentication);
        Long newAssignedTaskID = taskAssigneesService.assignTaskToUser(userID, taskID);
        TaskAssignees newTaskAssignee = taskAssigneesService.getAssignedTaskById(newAssignedTaskID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskAssignee);
    }

    @DeleteMapping("/{taskID}")
    @Transactional
    public ResponseEntity<Object> deleteTaskAssignment(@PathVariable("taskID") Long taskID,
                                                  @RequestParam(name = "userID") Long userID,
                                                  Authentication authentication) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(authentication);
        taskAssigneesService.deleteUserFromTask(userID, taskID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }


}
