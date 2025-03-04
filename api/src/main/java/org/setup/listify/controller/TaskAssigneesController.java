package org.setup.listify.controller;

import org.setup.listify.model.TaskAssignees;
import org.setup.listify.model.Users;
import org.setup.listify.service.TaskAssigneesService;
import org.setup.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks/assignee")
public class TaskAssigneesController {

    private final TaskAssigneesService taskAssigneesService;
    private final UserService userService;


    public TaskAssigneesController(TaskAssigneesService taskAssigneesService, UserService userService) {
        this.taskAssigneesService = taskAssigneesService;
        this.userService = userService;
    }


    @GetMapping("/{taskID}")
    public ResponseEntity<Object> getUsersAssignedToTask(@PathVariable("taskID") Long taskID,
                                                         Authentication authentication) {
        Long loggedInUserID = userService.getUserIDFromAuthentication(authentication);
        List<Users> usersAssignedToTask = taskAssigneesService.getUsersAssignedToTask(taskID, loggedInUserID);
        return ResponseEntity.ok(usersAssignedToTask);
    }


    @PostMapping("/{taskID}")
    @Transactional
    public ResponseEntity<Object> assignTask(@PathVariable("taskID") Long taskID,
                                         Authentication authentication,
                                        @RequestParam(name = "githubID") String githubID) {

        Long loggedInUserID = userService.getUserIDFromAuthentication(authentication);
        Long newAssignedTaskID = taskAssigneesService.assignTaskToUser(githubID, loggedInUserID, taskID);
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
