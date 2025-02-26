package org.setup.Listify.Controller;

import org.setup.Listify.Exception.ErrorResponse;
import org.setup.Listify.Model.TaskAssignees;
import org.setup.Listify.Assembler.TaskAssigneesModelAssembler;
import org.setup.Listify.Service.TaskAssigneesService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks/assigned")
public class TaskAssigneesController {

    private final TaskAssigneesService taskAssigneesService;
    private final TaskAssigneesModelAssembler assembler;

    public TaskAssigneesController(TaskAssigneesService taskAssigneesService, TaskAssigneesModelAssembler assembler) {
        this.taskAssigneesService = taskAssigneesService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<TaskAssignees>> getAllAssignedTasks() {
        List<TaskAssignees> assignedTasks = taskAssigneesService.getAllAssignedTasks();
        return assembler.toCollectionModel(assignedTasks);
    }

    @GetMapping("/{id}")
    public EntityModel<TaskAssignees> getAssignedTaskById(@PathVariable Long id) {
        TaskAssignees assignedTask = taskAssigneesService.getAssignedTaskById(id);
        return assembler.toModel(assignedTask);
    }

    @GetMapping("/user/{id}")
    public CollectionModel<EntityModel<TaskAssignees>> getTasksAssignedToSpecificUser(
            @PathVariable Long id) {

        List<TaskAssignees> tasksAssignedToUser = taskAssigneesService
                .getTasksAssignedToSpecificUser(id);

        return assembler.toCollectionModel(tasksAssignedToUser);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> assignTask(@RequestParam(required = false) Integer userID,
                                        @RequestParam(required = false) Integer taskID) {
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
    public ResponseEntity<?> deleteTaskAssignment(@PathVariable Long taskID,
                                                  @RequestParam(required = false) Integer userID,
                                                  @RequestParam(required = false) Integer teamLeaderID) {
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
