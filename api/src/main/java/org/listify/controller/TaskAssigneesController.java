package org.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.listify.model.TaskAssignees;
import org.listify.model.Users;
import org.listify.service.TaskAssigneesService;
import org.listify.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tasks/assignee")
public class TaskAssigneesController {

    private final TaskAssigneesService taskAssigneesService;
    private final UserService userService;


    public TaskAssigneesController(TaskAssigneesService taskAssigneesService, UserService userService) {
        this.taskAssigneesService = taskAssigneesService;
        this.userService = userService;
    }


    @GetMapping("/{taskID}")
    public ResponseEntity<List<Users>> getUsersAssignedToTask(@PathVariable("taskID") Long taskID,
                                                              HttpServletRequest request) {
        Long loggedInUserID = userService.getUserIDFromAuthentication(request);
        List<Users> usersAssignedToTask = taskAssigneesService.getUsersAssignedToTask(taskID, loggedInUserID);
        return ResponseEntity.ok(usersAssignedToTask);
    }


    @PostMapping("/{taskID}")
    @Transactional
    public ResponseEntity<TaskAssignees> assignTask(@PathVariable("taskID") Long taskID,
                                                    HttpServletRequest request,
                                        @RequestParam(name = "githubID") String githubID) {

        Long loggedInUserID = userService.getUserIDFromAuthentication(request);
        Long newAssignedTaskID = taskAssigneesService.assignTaskToUser(githubID, loggedInUserID, taskID);
        TaskAssignees newTaskAssignee = taskAssigneesService.getAssignedTaskById(newAssignedTaskID);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTaskAssignee);
    }


    @DeleteMapping("/{taskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskAssignment(@PathVariable("taskID") Long taskID,
                                                  @RequestParam(name = "githubID") String githubID,
                                                  HttpServletRequest request) {
        Long teamLeaderID = userService.getUserIDFromAuthentication(request);
        taskAssigneesService.deleteUserFromTask(githubID, taskID, teamLeaderID);
        return ResponseEntity.noContent().build();
    }


}
