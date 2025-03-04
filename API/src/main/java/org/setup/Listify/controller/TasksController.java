package org.setup.listify.controller;

import org.setup.listify.exception.ErrorResponse;
import org.setup.listify.model.Tasks;
import org.setup.listify.service.TasksService;
import org.setup.listify.assembler.TasksModelAssembler;
import org.setup.listify.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TasksController {

    private final TasksService tasksService;
    private final UserService userService;
    private final TasksModelAssembler assembler;

    public TasksController(TasksService tasksService, TasksModelAssembler assembler, UserService userService) {
        this.tasksService = tasksService;
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Tasks>> getAllTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userID = userService.getUserIDFromAuthentication(authentication);
        List<Tasks> tasks = tasksService.getAllTasks(userID);
        return assembler.toCollectionModel(tasks);
    }

    @GetMapping("/{id}")
    public EntityModel<Tasks> getTaskById(@PathVariable("id") Long id) {
        Tasks task = tasksService.getTaskById(id);
        return assembler.toModel(task);
    }

    @GetMapping("/subtask/{parentTaskID}")
    public CollectionModel<EntityModel<Tasks>> getAllSubtasksOfTask(@PathVariable("parentTaskID") Long parentTaskID) {
        List<Tasks> subtasks = tasksService.getAllSubtasksOfTask(parentTaskID);
        return assembler.toCollectionModel(subtasks);
    }

    @GetMapping("dependent/{taskID}")
    public EntityModel<Tasks> getDependentTaskByTaskId(@PathVariable("taskID") Long taskID) {
        Tasks dependentTask = tasksService.getDependentTaskById(taskID);
        return assembler.toModel(dependentTask);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> newTask(@RequestParam(name = "teamLeader", required = false) Integer teamLeaderID,
                                     @RequestParam(name = "projectID", required = false) Integer projectID,
                                     @RequestParam(name = "sectionID", required = false) Integer sectionID,
                                     @RequestParam(name = "taskName", required = false) String taskName,
                                     @RequestParam(name = "taskDescription", required = false) String taskDescription,
                                     @RequestParam(name = "taskPriority", required = false) Byte taskPriority,
                                     @RequestParam(name = "taskPosition", required = false) Byte taskPosition) {
        if (teamLeaderID == null || projectID == null || sectionID == null || taskName == null || taskPriority == null || taskPosition == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newTaskID = tasksService.createTask(teamLeaderID, projectID, sectionID, taskName, taskDescription, taskPriority, taskPosition);
        Tasks newTask = tasksService.getTaskById(newTaskID);
        EntityModel<Tasks> entityModel = assembler.toModel(newTask);
        return ResponseEntity.created((entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()))
                .body(entityModel);
    }

    @PostMapping("/subtask/{parentTaskID}")
    @Transactional
    public ResponseEntity<?> newSubTask(@PathVariable("parentTaskID") Long parentTaskID,
                                        Authentication authentication,
                                        @RequestParam(name = "taskName", required = false) String taskName,
                                        @RequestParam(name = "taskDescription", required = false) String taskDescription,
                                        @RequestParam(name = "sectionID", required = false) Integer sectionID,
                                        @RequestParam(name = "dueDate", required = false) LocalDateTime dueDate) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null || sectionID == null || taskName == null || dueDate == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Long newTaskID = tasksService.createSubTask(teamLeaderID, parentTaskID.intValue(), taskName, taskDescription, sectionID, dueDate);

        Tasks newTask = tasksService.getTaskById(newTaskID);
        EntityModel<Tasks> entityModel = assembler.toModel(newTask);
        return ResponseEntity.created((entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()))
                .body(entityModel);
    }

    @PutMapping("/{id}")
    @Transactional
    ResponseEntity<?> updateTask(@PathVariable("id") Long id,
                                 Authentication authentication,
                                 @RequestParam("newTaskName") String newTaskName,
                                 @RequestParam("newTaskDescription") String newTaskDescription,
                                 @RequestParam("newTaskPriority") Byte newTaskPriority,
                                 @RequestParam("newDueDate") LocalDateTime newDueDate) {
        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team Leader ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        tasksService.updateTaskDetails(id.intValue(), teamLeaderID, newTaskName, newTaskDescription, newTaskPriority, newDueDate);

        Tasks updatedTask = tasksService.getTaskById(id);
        EntityModel<Tasks> entityModel = assembler.toModel(updatedTask);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @PutMapping("/{id}/position")
    @Transactional
    ResponseEntity<?> updateTaskPosition(@PathVariable("id") Long id,
                                         Authentication authentication,
                                         @RequestParam(name = "newTaskPosition", required = false) Integer newTaskPosition,
                                         @RequestParam(name = "sectionID", required = false) Integer sectionID) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null || sectionID == null || newTaskPosition == null) {
            ErrorResponse errorResponse = new ErrorResponse("Missing required parameter(s). Please ensure all required parameters are provided.",
                    HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        tasksService.updateTaskPosition(teamLeaderID, id.intValue(), newTaskPosition, sectionID);

        Tasks updatedTask = tasksService.getTaskById(id);
        EntityModel<Tasks> entityModel = assembler.toModel(updatedTask);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @DeleteMapping("/{id}")
    @Transactional
    ResponseEntity<?> deleteTask(@PathVariable("id") Long id, Authentication authentication) {

        Long teamLeaderIDLong = userService.getUserIDFromAuthentication(authentication);
        int teamLeaderIDInt = teamLeaderIDLong.intValue();
        Integer teamLeaderID = Integer.valueOf(teamLeaderIDInt);

        if (teamLeaderID == null) {
            ErrorResponse errorResponse = new ErrorResponse("Team Leader ID is required.", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        tasksService.deleteTaskById(id, teamLeaderID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Task with id: "+ id +" has been successfully deleted"));
    }
}
