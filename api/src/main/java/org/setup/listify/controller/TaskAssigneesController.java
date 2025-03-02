package org.setup.listify.controller;

import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.TaskAssignees;
import org.setup.listify.service.TaskAssigneesService;
import org.setup.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> getAllAssignedTasks() {
        List<TaskAssignees> assignedTasks = taskAssigneesService.getAllAssignedTasks();
        if (assignedTasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There are no assigned tasks");
        }
        return ResponseEntity.ok(assignedTasks);
    }


    @GetMapping("/user/{userID}")
    public ResponseEntity<?> getTasksAssignedToSpecificUser(
            @PathVariable("userID") Long userID) {

        List<TaskAssignees> tasksAssignedToUser = taskAssigneesService
                .getTasksAssignedToSpecificUser(userID);

        if (tasksAssignedToUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There no tasks assigned to user: "+userID);
        }
        return ResponseEntity.ok(tasksAssignedToUser);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> assignTask(Authentication authentication,
                                        @RequestParam(name = "taskID", required = false) Integer taskID) {
        Long userIDLong = userService.getUserIDFromAuthentication(authentication);
        int userID = userIDLong.intValue();

        if (taskID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Task ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Long newAssignedTaskID = taskAssigneesService.assignTaskToUser(userID, taskID);
        TaskAssignees newTaskAssignee = taskAssigneesService.getAssignedTaskById(newAssignedTaskID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskAssignee);
    }

    @DeleteMapping("/{taskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskAssignment(@PathVariable("taskID") Long taskID,
                                                  @RequestParam(name = "userID", required = false) Integer userID,
                                                  Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        if (userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        taskAssigneesService.deleteUserFromTask(userID, taskID.intValue(), teamLeaderID);
        return ResponseEntity.noContent().build();
    }


}
