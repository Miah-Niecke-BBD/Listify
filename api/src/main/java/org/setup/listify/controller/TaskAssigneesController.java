package org.setup.listify.controller;

import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.TaskAssignees;
import org.setup.listify.assembler.TaskAssigneesModelAssembler;
import org.setup.listify.service.TaskAssigneesService;
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
@RequestMapping("/tasks/assigned")
public class TaskAssigneesController {

    private final TaskAssigneesService taskAssigneesService;
    private final UserService userService;
    private final TaskAssigneesModelAssembler assembler;

    public TaskAssigneesController(TaskAssigneesService taskAssigneesService, TaskAssigneesModelAssembler assembler, UserService userService) {
        this.taskAssigneesService = taskAssigneesService;
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<TaskAssignees>> getAllAssignedTasks() {
        List<TaskAssignees> assignedTasks = taskAssigneesService.getAllAssignedTasks();
        return assembler.toCollectionModel(assignedTasks);
    }

    @GetMapping("/{id}")
    public EntityModel<TaskAssignees> getAssignedTaskById(@PathVariable("id") Long id) {
        TaskAssignees assignedTask = taskAssigneesService.getAssignedTaskById(id);
        return assembler.toModel(assignedTask);
    }

    @GetMapping("/user/{id}")
    public CollectionModel<EntityModel<TaskAssignees>> getTasksAssignedToSpecificUser(
            @PathVariable("id") Long id) {

        List<TaskAssignees> tasksAssignedToUser = taskAssigneesService
                .getTasksAssignedToSpecificUser(id);

        return assembler.toCollectionModel(tasksAssignedToUser);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> assignTask(Authentication authentication,
                                        @RequestParam(name = "taskID", required = false) Integer taskID) {
        Long userIDLong = userService.getUserIDFromAuthentication(authentication);
        int userIDInt = userIDLong.intValue();
        Integer userID = Integer.valueOf(userIDInt);

        if (userID == null || taskID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID and Task ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        Long newAssignedTaskID = taskAssigneesService.assignTaskToUser(userID, taskID);
        TaskAssignees newTaskAssignee = taskAssigneesService.getAssignedTaskById(newAssignedTaskID);
        EntityModel<TaskAssignees> entityModel = assembler.toModel(newTaskAssignee);

        return ResponseEntity.created((entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()))
                .body(entityModel);
    }

    @DeleteMapping("/{taskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskAssignment(@PathVariable("taskID") Long taskID,
                                                  @RequestParam(name = "userID", required = false) Integer userID,
                                                  Authentication authentication) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null || userID == null) {
            ErrorResponse errorResponse = new ErrorResponse("User ID and Team Leader ID are required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        taskAssigneesService.deleteUserFromTask(userID, taskID.intValue(), teamLeaderID);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Task with id: "+ taskID +" is no longer assigned to user "+userID,
                        "status", HttpStatus.NO_CONTENT));
    }


}
