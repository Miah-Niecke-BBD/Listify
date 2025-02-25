package org.setup.Listify.Controller;

import org.setup.Listify.Model.TaskAssignees;
import org.setup.Listify.Service.TaskAssigneesModelAssembler;
import org.setup.Listify.Service.TaskAssigneesService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TaskAssigneesController {

    private final TaskAssigneesService taskAssigneesService;
    private final TaskAssigneesModelAssembler assembler;

    public TaskAssigneesController(TaskAssigneesService taskAssigneesService, TaskAssigneesModelAssembler assembler) {
        this.taskAssigneesService = taskAssigneesService;
        this.assembler = assembler;
    }

    @GetMapping("/tasks/assigned")
    public CollectionModel<EntityModel<TaskAssignees>> getAllAssignedTasks() {
        List<TaskAssignees> assignedTasks = taskAssigneesService.getAllAssignedTasks();
        return assembler.toCollectionModel(assignedTasks);
    }

    @GetMapping("/tasks/assigned/{id}")
    public EntityModel<TaskAssignees> getAssignedTaskById(@PathVariable Long id) {
        TaskAssignees assignedTask = taskAssigneesService.getAssignedTaskById(id);
        return assembler.toModel(assignedTask);
    }

    @GetMapping("/tasks/assigned/user/{id}")
    public CollectionModel<EntityModel<TaskAssignees>> getTasksAssignedToSpecificUser(
            @PathVariable Long id) {

        List<TaskAssignees> tasksAssignedToUser = taskAssigneesService
                .getTasksAssignedToSpecificUser(id);

        return assembler.toCollectionModel(tasksAssignedToUser);
    }

    @PostMapping("/tasks/assigned")
    @Transactional
    public ResponseEntity<?> assignTask(@RequestParam int userID,
                                        @RequestParam int taskID) {
        taskAssigneesService.assignTaskToUser(userID, taskID);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Task: "+ taskID+ " is successfully assigned to User: "+ userID,
                        "status", HttpStatus.CREATED));
    }

    @DeleteMapping("/tasks/assigned/{taskID}")
    @Transactional
    public ResponseEntity<?> deleteTaskAssignment(@PathVariable Long taskID,
                                                  @RequestParam int userID,
                                                  @RequestParam int teamLeaderID) {
        taskAssigneesService.deleteUserFromTask(userID, taskID.intValue(), teamLeaderID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(Map.of("message", "Task with id: "+ taskID +" is no longer assigned to user "+userID,
                        "status", HttpStatus.NO_CONTENT));
    }


}
