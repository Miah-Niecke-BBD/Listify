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

        List<UserAssignedTasksDTO> tasksAssignedToUser = taskAssigneesService
                .getTasksAssignedToSpecificUser(userID);

        if (tasksAssignedToUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There no tasks assigned to user: "+userID);
        }
        return ResponseEntity.ok(tasksAssignedToUser);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> assignTask(Authentication authentication,
                                        @RequestParam(name = "taskID") Integer taskID) {
        Long userIDLong = userService.getUserIDFromAuthentication(authentication);
        int userID = userIDLong.intValue();

        Long newAssignedTaskID = taskAssigneesService.assignTaskToUser(userID, taskID);
        TaskAssignees newTaskAssignee = taskAssigneesService.getAssignedTaskById(newAssignedTaskID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskAssignee);
    }

    @DeleteMapping("/{taskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskAssignment(@PathVariable("taskID") Long taskID,
                                                  @RequestParam(name = "userID") Integer userID,
                                                  Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderID = teamLeaderIDLong.intValue();

        taskAssigneesService.deleteUserFromTask(userID, taskID.intValue(), teamLeaderID);
        return ResponseEntity.noContent().build();
    }


}
